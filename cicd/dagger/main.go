package main

import (
	"context"
	"errors"
	"fmt"
	"strings"
)

type VookServer struct{}

func (v *VookServer) BuildApiJar(
	ctx context.Context,
	// 빌드 대상의 디렉토리
	dir *Directory,
	// +optional
	test bool,
) (*File, error) {
	c := dag.Container().
		From("eclipse-temurin:21-jdk").
		WithWorkdir("/app").
		WithDirectory("/app/gradle", dir.Directory("gradle")).
		WithFiles("/app", []*File{dir.File("gradlew")}).
		WithFiles("/app", []*File{
			dir.File("build.gradle"),
			dir.File("settings.gradle"),
		}).
		WithExec([]string{"./gradlew"}).
		WithDirectory("/app/src", dir.Directory("src"))

	if test {
		_, err := c.
			WithExec([]string{"./gradlew", "test"}).
			Sync(ctx)
		if err != nil {
			return nil, errors.New("test fail:" + err.Error())
		}
	}

	jarFile := c.
		WithExec([]string{"./gradlew", "bootJar"}).
		File("jar/api.jar")

	return jarFile, nil
}

func (v *VookServer) BuildApiImage(
	// jar 파일
	jarFile *File,
	// profile
	// +optional
	profile []string,
) *File {
	if profile == nil {
		profile = []string{"default"}
	}

	dockerfile := `
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY app.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=` + strings.Join(profile, ",") + `", "app.jar"]
`
	sourceDir := dag.Directory().
		WithFile("app.jar", jarFile).
		WithNewFile("Dockerfile", dockerfile)

	return dag.Docker().
		Build(sourceDir, DockerBuildOpts{
			Platform: []Platform{"linux/arm64"},
		}).
		Save(DockerBuildSaveOpts{
			Name: "api",
		}).
		File("api_linux_arm64.tar")
}

func (v *VookServer) SendImage(
	ctx context.Context,
	sshDest *Secret,
	sshKey *Secret,
	path string,
	imageTar *File,
) error {
	sshDestText, err := sshDest.Plaintext(ctx)
	if err != nil {
		return err
	}

	_, err = dag.Scp().
		Config(strings.TrimSpace(sshDestText)).
		WithIdentityFile(sshKey).
		FileToRemote(imageTar, ScpCommanderFileToRemoteOpts{
			Target: path,
		}).
		Sync(ctx)
	if err != nil {
		return err
	}

	return nil
}

func (v *VookServer) Apply(
	ctx context.Context,
	destination *Secret,
	sshKey *Secret,
	imageTar *File,
	path string,
	version string,
	command string,
) error {
	destinationText, err := destination.Plaintext(ctx)
	if err != nil {
		return err
	}

	filename, err := imageTar.Name(ctx)
	if err != nil {
		return err
	}

	_, err = dag.SSH().
		Config(strings.TrimSpace(destinationText)).
		WithIdentityFile(sshKey).
		Command(
			fmt.Sprintf(`
cd %s
API_FILENAME=%s API_VERSION=%s %s
`, path, filename, version, command),
		).
		Sync(ctx)
	if err != nil {
		return err
	}

	return nil
}

func (v *VookServer) Deploy(
	ctx context.Context,
	sourceDir *Directory,
	profile string,
	sshDest *Secret,
	sshKey *Secret,
	targetPath string,
	version string,
	command string,
) error {
	jarFile, err := v.BuildApiJar(ctx, sourceDir, true)
	if err != nil {
		return err
	}

	imageTar := v.BuildApiImage(jarFile, []string{"default", profile})

	err = v.SendImage(ctx, sshDest, sshKey, targetPath, imageTar)
	if err != nil {
		return err
	}

	err = v.Apply(ctx, sshDest, sshKey, imageTar, targetPath, version, command)
	if err != nil {
		return err
	}

	return nil
}
