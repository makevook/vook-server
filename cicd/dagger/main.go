package main

import (
	"context"
	"dagger/vook-server/internal/dagger"
	"errors"
	"fmt"
	"strings"
)

type VookServer struct{}

func (v *VookServer) BuildJar(
	ctx context.Context,
	// 빌드 대상의 디렉토리
	dir *dagger.Directory,
	// +optional
	test bool,
	// +optional
	subModule string,
) (*dagger.File, error) {
	c := dag.Java().
		Init().
		WithGradleCache().
		WithDir(dir).
		Container()

	if test {
		var testCommand []string
		if subModule == "" {
			testCommand = []string{"./gradlew", "test"}
		} else {
			testCommand = []string{"./gradlew", fmt.Sprintf(":%s:test", subModule)}
		}

		_, err := c.
			With(dag.DockerService().WithCacheVolume("docker-var/lib/docker").BindAsService).
			WithExec(testCommand).
			Sync(ctx)
		if err != nil {
			return nil, errors.New("test fail:" + err.Error())
		}
	}

	var bootJarCommand []string
	if subModule == "" {
		bootJarCommand = []string{"./gradlew", "bootJar"}
	} else {
		bootJarCommand = []string{"./gradlew", fmt.Sprintf(":%s:bootJar", subModule)}
	}

	jarFile := c.
		WithExec(bootJarCommand).
		File(fmt.Sprintf("jar/%s.jar", subModule))

	return jarFile, nil
}

func (v *VookServer) BuildImage(
	// jar 파일
	jarFile *dagger.File,
	// profile
	// +optional
	profile []string,
	// 이미지 이름
	name string,
) *dagger.File {
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
		Build(sourceDir, dagger.DockerBuildOpts{
			Platform: []dagger.Platform{"linux/arm64"},
		}).
		Save(dagger.DockerBuildSaveOpts{
			Name: name,
		}).
		File(fmt.Sprintf("%s_linux_arm64.tar", name))
}

func (v *VookServer) SendImage(
	ctx context.Context,
	sshDest *dagger.Secret,
	sshKey *dagger.Secret,
	path string,
	imageTar *dagger.File,
) error {
	sshDestText, err := sshDest.Plaintext(ctx)
	if err != nil {
		return err
	}

	_, err = dag.Scp().
		Config(strings.TrimSpace(sshDestText)).
		WithIdentityFile(sshKey).
		FileToRemote(imageTar, dagger.ScpCommanderFileToRemoteOpts{
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
	destination *dagger.Secret,
	sshKey *dagger.Secret,
	imageTar *dagger.File,
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
	sourceDir *dagger.Directory,
	profile string,
	sshDest *dagger.Secret,
	sshKey *dagger.Secret,
	targetPath string,
	version string,
	command string,
) error {
	jarFile, err := v.BuildJar(ctx, sourceDir, true, "api")
	if err != nil {
		return err
	}

	imageTar := v.BuildImage(jarFile, []string{"default", profile}, "api")

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
