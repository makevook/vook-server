package vook.server.api.web.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import vook.server.api.model.values.AuthValues;

@OpenAPIDefinition(
        info = @Info(title = "Vook Server API", version = "0.1", description = "Vook Server API"),
        servers = {@Server(url = "/", description = "Default Server URL")},
        security = {
                @SecurityRequirement(name = "AccessToken")
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "AccessToken",
                description = "JWT 인증 토큰",
                type = SecuritySchemeType.APIKEY,
                in = SecuritySchemeIn.HEADER,
                paramName = AuthValues.AUTHORIZATION_HEADER
        )
})
@Configuration
public class OpenApiDefinition {
}
