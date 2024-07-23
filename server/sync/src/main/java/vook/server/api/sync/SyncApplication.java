package vook.server.api.sync;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import vook.server.api.config.QuerydslConfig;
import vook.server.api.devhelper.app.sync.SyncService;

@SpringBootApplication
@Import({QuerydslConfig.class})
@ComponentScan(basePackages = "vook.server.api.infra.search", basePackageClasses = SyncService.class)
@EntityScan(basePackages = "vook.server.api.domain")
public class SyncApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SyncApplication.class)
                .web(WebApplicationType.NONE)
                .run(args).close();
    }

    @Component
    @RequiredArgsConstructor
    public static class Runner implements ApplicationRunner {

        private final SyncService syncService;

        @Override
        public void run(ApplicationArguments args) {
            syncService.sync();
        }
    }
}
