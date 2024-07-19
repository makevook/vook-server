package vook.server.api.testhelper;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MeilisearchContainer extends GenericContainer<MeilisearchContainer> {

    private static final String DEFAULT_IMAGE_NAME = "getmeili/meilisearch:latest";

    public MeilisearchContainer() {
        this(DEFAULT_IMAGE_NAME);
    }

    public MeilisearchContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public MeilisearchContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        this.addExposedPort(7700);
        this.addEnv("MEILI_MASTER_KEY", "aMasterKey");
    }

    public String getMasterKey() {
        return this.getEnvMap().get("MEILI_MASTER_KEY");
    }

    /**
     * Meilisearch에 접속하기 위한 URL을 반환한다.
     */
    public String getHostUrl() {
        return "http://" + getHost() + ":" + getMappedPort(7700);
    }
}
