package vook.server.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import vook.server.api.web.common.swagger.GlobalOpenApiCustomizerImpl;
import vook.server.api.web.common.swagger.GlobalOperationCustomizerImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

@Configuration
public class SwaggerConfig {

    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return new GlobalOpenApiCustomizerImpl();
    }

    @Bean
    public GlobalOperationCustomizer globalOperationCustomizer(Supplier<OpenAPI> openAPISupplier) {
        return new GlobalOperationCustomizerImpl(openAPISupplier);
    }

    // GlobalOperationCustomizer에서 OpenAPI가 가지고 있는 Contents에 접근해야 될 필요가 생김
    // GlobalOpenApiCustomizer는 GlobalOperationCustomizer 보다 나중에 호출되므로 GlobalOpenApiCustomizer에서 GlobalOperationCustomizer로 OpenAPI를 전달하는 것을 불가능
    // OpenAPIService.build에서 최초로 OpenAPI를 생성하므로 (AbstractOpenApiResource.java:336), OpenAPI 생성 후 OpenAPI를 제공하는 기능을 추가한 커스텀 클래스를 대신 만들어 사용
    @Bean
    @Lazy(value = false)
    public OpenAPIServiceWithRegistration openAPIServiceWithRegistration(
            Optional<OpenAPI> openAPI,
            SecurityService securityParser,
            SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils,
            Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers,
            Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomisers, Optional<JavadocProvider> javadocProvider
    ) {
        return new OpenAPIServiceWithRegistration(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomisers, serverBaseUrlCustomisers, javadocProvider);
    }

    public static class OpenAPIServiceWithRegistration extends OpenAPIService implements Supplier<OpenAPI> {

        private OpenAPI openAPI;

        public OpenAPIServiceWithRegistration(Optional<OpenAPI> openAPI, SecurityService securityParser, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils, Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers, Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers, Optional<JavadocProvider> javadocProvider) {
            super(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
        }

        @Override
        public OpenAPI build(Locale locale) {
            OpenAPI openAPI = super.build(locale);
            this.openAPI = openAPI;
            return openAPI;
        }

        @Override
        public OpenAPI get() {
            return this.openAPI;
        }
    }
}
