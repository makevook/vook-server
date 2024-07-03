package vook.server.api.web.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse;
import vook.server.api.web.term.reqres.*;

import java.util.List;

import static vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse.Kind.INVALID_PARAMETER;
import static vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse.Kind.VIOLATION_BUSINESS_RULE;

@Tag(name = "term", description = "용어 API")
public interface TermApi {

    @Operation(
            summary = "용어 추가",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                                        
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
    @IncludeBadRequestResponse({INVALID_PARAMETER, VIOLATION_BUSINESS_RULE})
    CommonApiResponse<TermCreateResponse> create(VookLoginUser user, TermCreateRequest request);

    class TermApiCreateResponse extends CommonApiResponse<TermCreateResponse> {
    }

    @Operation(
            summary = "용어 조회",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 허용하는 sort 키워드
                    term, synonym, meaning, createdAt
                                        
                    사용 예시
                    - term,asc
                    - synonym,desc
                    - meaning,desc
                    - createdAt,asc
                                        
                    ## 비즈니스 규칙 위반 내용
                    - VocabularyNotFound: 사용자의 용어집 중 해당 ID의 용어집이 존재하지 않는 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TermApiRetrieveResponse.class)
                    )
            ),
    })
    @IncludeBadRequestResponse(VIOLATION_BUSINESS_RULE)
    CommonApiResponse<List<TermResponse>> retrieve(VookLoginUser user, @ParameterObject Pageable pageable, String vocabularyUid);

    class TermApiRetrieveResponse extends CommonApiResponse<TermResponse> {
    }

    @Operation(
            summary = "용어 수정",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                    - TermNotFound: 삭제하려는 용어가 존재하지 않는 경우
                    - NotValidVocabularyOwner: 조회하려는 용어가 속해있는 용어집에 대한 권한이 없는 경우"""
    )
    @IncludeBadRequestResponse({INVALID_PARAMETER, VIOLATION_BUSINESS_RULE})
    CommonApiResponse<Void> update(VookLoginUser user, String termUid, TermUpdateRequest request);

    @Operation(
            summary = "용어 삭제",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                    - TermNotFound: 삭제하려는 용어가 존재하지 않는 경우
                    - NotValidVocabularyOwner: 삭제하려는 용어가 속해있는 용어집에 대한 권한이 없는 경우"""
    )
    @IncludeBadRequestResponse(VIOLATION_BUSINESS_RULE)
    CommonApiResponse<Void> delete(VookLoginUser user, String termUid);

    @Operation(
            summary = "용어 다중 삭제",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                    - TermNotFound: 삭제하려는 용어가 존재하지 않는 경우
                    - NotValidVocabularyOwner: 삭제하려는 용어가 속해있는 용어집에 대한 권한이 없는 경우"""
    )
    @IncludeBadRequestResponse({INVALID_PARAMETER, VIOLATION_BUSINESS_RULE})
    CommonApiResponse<Void> batchDelete(VookLoginUser user, TermBatchDeleteRequest request);

    @Operation(
            summary = "용어 검색",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
                    - NotValidVocabularyOwnerException: 검색 요청한 용어집 UID에 대한 권한이 없는 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TermApiSearchResponse.class)
                    )
            ),
    })
    @IncludeBadRequestResponse({INVALID_PARAMETER, VIOLATION_BUSINESS_RULE})
    CommonApiResponse<TermSearchResponse> search(VookLoginUser user, TermSearchRequest request);

    class TermApiSearchResponse extends CommonApiResponse<TermSearchResponse> {
    }
}
