package vook.server.api.config;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vook.server.api.web.swagger.GlobalOpenApiCustomizerImpl;
import vook.server.api.web.swagger.GlobalOperationCustomizerImpl;

@Configuration
public class SwaggerConfig {

    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return new GlobalOpenApiCustomizerImpl();
    }

    @Bean
    public GlobalOperationCustomizer globalOperationCustomizer() {
        return new GlobalOperationCustomizerImpl();
    }
}
