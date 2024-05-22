package vook.server.api.testhelper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;

import java.util.Map;
import java.util.TimeZone;

import static vook.server.api.config.TimeZoneConfig.DEFAULT_TIME_ZONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiTest {

    @Autowired
    protected TestRestTemplate rest;

    @ServiceConnection
    protected static final MariaDBContainer mariaDBContainer = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("example")
            .withUsername("user")
            .withPassword("userPw")
            .withConfigurationOverride("db/conf")
            .withTmpFs(Map.of("/var/lib/mysql", "rw"));

    static {
        mariaDBContainer.start();
    }

    @BeforeEach
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
    }
}
