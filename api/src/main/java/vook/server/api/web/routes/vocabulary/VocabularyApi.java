package vook.server.api.web.routes.vocabulary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyUpdateRequest;
import vook.server.api.web.swagger.ComponentRefConsts;

import java.util.List;

@Tag(name = "vocabulary", description = "용어집 API")
public interface VocabularyApi {

    @Operation(
            summary = "용어집 조회",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyApiVocabulariesResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<VocabularyResponse>> vocabularies(VookLoginUser user);

    class VocabularyApiVocabulariesResponse extends CommonApiResponse<List<VocabularyResponse>> {
    }

    @Operation(
            summary = "용어집 생성",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - VocabularyLimitExceeded: 사용자의 용어집 생성 제한을 초과하여 용어집을 생성할 수 없는 경우 (3개 초과)"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = ComponentRefConsts.Schema.COMMON_API_RESPONSE),
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 파라미터", ref = ComponentRefConsts.Example.INVALID_PARAMETER),
                                    @ExampleObject(name = "비즈니스 규칙 위반", ref = ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE)
                            }
                    )
            ),
    })
    CommonApiResponse<Void> createVocabulary(VookLoginUser user, VocabularyCreateRequest request);

    @Operation(
            summary = "용어집 수정",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - VocabularyNotFound: 사용자의 용어집 중 해당 ID의 용어집이 존재하지 않는 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = ComponentRefConsts.Schema.COMMON_API_RESPONSE),
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 파라미터", ref = ComponentRefConsts.Example.INVALID_PARAMETER),
                                    @ExampleObject(name = "비즈니스 규칙 위반", ref = ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE)
                            }
                    )
            ),
    })
    CommonApiResponse<Void> updateVocabulary(
            VookLoginUser user,
            String vocabularyUid,
            VocabularyUpdateRequest request
    );
}
