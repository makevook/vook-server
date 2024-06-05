package vook.server.api.testhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public abstract class WebApiTest extends IntegrationTestBase {

    @Autowired
    protected TestRestTemplate rest;
}
