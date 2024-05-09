package vook.server.api.testhelper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.TimeZone;

import static vook.server.api.config.TimeZoneConfig.DEFAULT_TIME_ZONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiTest {

    @Autowired
    protected TestRestTemplate rest;

    @BeforeEach
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
    }
}
