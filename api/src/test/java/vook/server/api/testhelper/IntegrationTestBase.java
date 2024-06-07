package vook.server.api.testhelper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MariaDBContainer;
import vook.server.api.outbound.search.MeilisearchProperties;

import java.util.Map;
import java.util.TimeZone;

import static vook.server.api.config.TimeZoneConfig.DEFAULT_TIME_ZONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestBase {

    @ServiceConnection
    protected static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("example")
            .withUsername("user")
            .withPassword("userPw")
            .withConfigurationOverride("db/conf")
            .withTmpFs(Map.of("/var/lib/mysql", "rw"));

    protected static final MeilisearchContainer meilisearchContainer = new MeilisearchContainer("getmeili/meilisearch:v1.8.0");

    static {
        mariaDBContainer.start();
        meilisearchContainer.start();
    }

    @Autowired
    protected TestRestTemplate rest;

    @BeforeEach
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
    }

    @Configuration
    public static class TestConfig {

        @Bean
        public MeilisearchProperties meilisearchProperties() {
            MeilisearchProperties meilisearchProperties = new MeilisearchProperties();
            meilisearchProperties.setHost(meilisearchContainer.getHostUrl());
            meilisearchProperties.setApiKey(meilisearchContainer.getMasterKey());
            return meilisearchProperties;
        }
    }
}
