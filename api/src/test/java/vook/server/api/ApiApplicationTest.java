package vook.server.api;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ApiApplicationTest {

    @Test
    void modulithTest() {
        ApplicationModules modules = ApplicationModules.of(ApiApplication.class);
        modules.forEach(System.out::println);
        modules.verify();

        new Documenter(modules).writeDocumentation();
    }
}
