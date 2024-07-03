package vook.server.api.testhelper;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import vook.server.api.infra.search.common.MeilisearchProperties;

import java.util.Map;

@Testcontainers(parallel = true)
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>("mariadb:10.11.8")
                .withDatabaseName("example")
                .withUsername("user")
                .withPassword("userPw")
                .withConfigurationOverride("db/conf")
                .withTmpFs(Map.of("/var/lib/mysql", "rw"));
    }

    @Bean
    MeilisearchContainer meilisearchContainer() {
        return new MeilisearchContainer("getmeili/meilisearch:v1.8.3");
    }

    @Bean
    public MeilisearchProperties meilisearchProperties(MeilisearchContainer meilisearchContainer) {
        MeilisearchProperties meilisearchProperties = new MeilisearchProperties();
        meilisearchProperties.setHost(meilisearchContainer.getHostUrl());
        meilisearchProperties.setApiKey(meilisearchContainer.getMasterKey());
        return meilisearchProperties;
    }
}