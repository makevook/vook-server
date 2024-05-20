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
        info = @Info(
                title = "Vook Server API",
                version = "0.1",
                description = """
                        Vook 서버 API 문서입니다.
                                                
                        ## Authorization (토큰 획득 방법)
                                                
                        - [구글 로그인](/oauth2/authorization/google)을 통해 로그인 한 후, 
                        redirect URL에 포함된 accessToken을 사용합니다. refreshToken은 토큰 갱신 때 사용합니다."""),
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
