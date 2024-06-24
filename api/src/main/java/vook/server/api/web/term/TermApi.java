package vook.server.api.web.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.term.reqres.TermCreateRequest;
import vook.server.api.web.term.reqres.TermCreateResponse;
import vook.server.api.web.term.reqres.TermUpdateRequest;

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
                    - TermLimitExceeded: 사용자의 용어집에 용어를 추가할 수 있는 제한을 초과한 경우
                    - NotValidVocabularyOwner: 용어를 추가하려는 용어집에 대한 권한이 없는 경우"""
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

    @Operation(
            summary = "용어 수정",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - TermNotFound: 사용자의 용어집 중 해당 ID의 용어가 존재하지 않는 경우
                    - NotValidVocabularyOwner: 수정하려는 용어가 속해있는 용어집에 대한 권한이 없는 경우"""
    )
    CommonApiResponse<Void> update(VookLoginUser user, TermUpdateRequest request);
}
