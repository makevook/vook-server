package vook.server.poc.springdataredis.testhelper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public abstract class IntegrationTestBase {
}
