package vook.server.api.web.routes.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.demo.reqres.RetrieveGlossariesResponse;
import vook.server.api.web.routes.demo.reqres.RetrieveTermsResponse;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

import java.util.List;

@Tag(name = "demo", description = "VooK 데모용 API")
public interface DemoApi {

    @Operation(summary = "용어집 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RetrieveGlossariesApiResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<RetrieveGlossariesResponse>> retrieveGlossaries();

    class RetrieveGlossariesApiResponse extends CommonApiResponse<List<RetrieveGlossariesResponse>> {
    }

    @Operation(summary = "용어집 내 용어 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RetrieveTermsApiResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<RetrieveTermsResponse>> retrieveTerms(
            String glossaryUid,
            @ParameterObject Pageable pageable
    );

    class RetrieveTermsApiResponse extends CommonApiResponse<List<RetrieveTermsResponse>> {
    }

    @Operation(summary = "용어 검색")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = SearchApiTermResponse.class)
                    )
            ),
    })
    CommonApiResponse<SearchTermResponse> searchTerm(
            String glossaryUid,
            SearchTermRequest request
    );

    class SearchApiTermResponse extends CommonApiResponse<SearchTermResponse> {
    }
}
