package vook.server.api.web.vocabulary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse;
import vook.server.api.web.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.vocabulary.reqres.VocabularyResponse;
import vook.server.api.web.vocabulary.reqres.VocabularyUpdateRequest;

import java.util.List;

import static vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse.Kind.INVALID_PARAMETER;
import static vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse.Kind.VIOLATION_BUSINESS_RULE;

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
                    ## 비즈니스 규칙 위반 내용
                    - VocabularyLimitExceeded: 사용자의 용어집 생성 제한을 초과하여 용어집을 생성할 수 없는 경우 (3개 초과)"""
    )
    @IncludeBadRequestResponse({INVALID_PARAMETER, VIOLATION_BUSINESS_RULE})
    CommonApiResponse<Void> createVocabulary(VookLoginUser user, VocabularyCreateRequest request);

    @Operation(
            summary = "용어집 수정",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                    - VocabularyNotFound: 사용자의 용어집 중 해당 ID의 용어집이 존재하지 않는 경우"""
    )
    @IncludeBadRequestResponse({INVALID_PARAMETER, VIOLATION_BUSINESS_RULE})
    CommonApiResponse<Void> updateVocabulary(
            VookLoginUser user,
            String vocabularyUid,
            VocabularyUpdateRequest request
    );

    @Operation(
            summary = "용어집 삭제",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                    - VocabularyNotFound: 사용자의 용어집 중 해당 ID의 용어집이 존재하지 않는 경우"""
    )
    @IncludeBadRequestResponse(VIOLATION_BUSINESS_RULE)
    CommonApiResponse<Void> deleteVocabulary(VookLoginUser user, String vocabularyUid);
}
