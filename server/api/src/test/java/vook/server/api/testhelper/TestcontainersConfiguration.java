package vook.server.api.testhelper;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import vook.server.api.infra.search.common.MeilisearchProperties;

import java.util.Map;

@Testcontainers(parallel = true)
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MariaDbWithMigrate<?> mariaDBContainer() {
        return new MariaDbWithMigrate<>("mariadb:10.11.8")
                .withDatabaseName("example")
                .withUsername("user")
                .withPassword("userPw")
                .withConfigurationOverride("db/conf")
                .withTmpFs(Map.of("/var/lib/mysql", "rw"));
    }

    public static class MariaDbWithMigrate<SELF extends MariaDbWithMigrate<SELF>> extends MariaDBContainer<SELF> {
        public MariaDbWithMigrate(String dockerImageName) {
            super(dockerImageName);
        }

        @Override
        public void start() {
            super.start();
            migrateContainer(this).start();
        }

        private GenericContainer<?> migrateContainer(MariaDBContainer<?> db) {
            return new GenericContainer<>(DockerImageName.parse("migrate/migrate:v4.17.1"))
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("migrate/sql/"),
                            "/sql"
                    )
                    .withCommand(
                            "-source=file:///sql",
                            "-database=mysql://%s:%s@tcp(%s:3306)/%s".formatted(
                                    db.getUsername(), db.getPassword(), db.getContainerInfo().getNetworkSettings().getIpAddress(), db.getDatabaseName()
                            ),
                            "up"
                    );
        }
    }

    @Bean
    MeilisearchContainer meilisearchContainer() {
        return new MeilisearchContainer("getmeili/meilisearch:v1.9.0");
    }

    @Bean
    public MeilisearchProperties meilisearchProperties(MeilisearchContainer meilisearchContainer) {
        MeilisearchProperties meilisearchProperties = new MeilisearchProperties();
        meilisearchProperties.setHost(meilisearchContainer.getHostUrl());
        meilisearchProperties.setApiKey(meilisearchContainer.getMasterKey());
        return meilisearchProperties;
    }

    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer() {
        return new GenericContainer<>(DockerImageName.parse("redis:7.2.5")).withExposedPorts(6379);
    }
}
