package vook.server.api.web.routes.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.common.web.CommonApiResponse;
import vook.server.api.web.routes.term.reqres.TermCreateRequest;
import vook.server.api.web.routes.term.reqres.TermCreateResponse;

@Tag(name = "term", description = "용어 API")
public interface TermApi {

    @Operation(
            summary = "용어 추가",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - VocabularyNotFound: 사용자의 용어집 중 해당 ID의 용어집이 존재하지 않는 경우
                    - TermLimitExceeded: 사용자의 용어집에 용어를 추가할 수 있는 제한을 초과한 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TermApiCreateResponse.class)
                    )
            ),
    })
    CommonApiResponse<TermCreateResponse> create(VookLoginUser user, TermCreateRequest request);

    class TermApiCreateResponse extends CommonApiResponse<TermCreateResponse> {
    }
}
