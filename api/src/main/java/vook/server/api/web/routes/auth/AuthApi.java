package vook.server.api.web.routes.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import vook.server.api.model.values.AuthValues;

@Tag(name = "auth", description = "인증 관련 API")
public interface AuthApi {

    @Operation(
            summary = "토큰 갱신",
            description = """
                    리프레시 토큰을 이용하여 엑세스 토큰과 리프레시 토큰을 갱신합니다.
                    리프레시 토큰은 최상위 Description에 Authorzation 항목을 참고하세요."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    headers = {
                            @Header(name = AuthValues.AUTHORIZATION_HEADER, description = "갱신된 엑세스 토큰"),
                            @Header(name = AuthValues.REFRESH_AUTHORIZATION_HEADER, description = "갱신된 리프레시 토큰")
                    }
            ),
    })
    ResponseEntity<Void> refreshToken(String refresh, HttpServletResponse response);
}
