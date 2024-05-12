package vook.server.api.web.routes.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.CommonApiResponse;

@Tag(name = "term", description = "용어 API")
public interface TermRestApi {

    @Operation(summary = "용어 검색")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = SearchApiResponse.class)
                    )
            ),
    })
    CommonApiResponse<SearchResponse> search(SearchRequest request);

    class SearchApiResponse extends CommonApiResponse<SearchResponse> {
    }
}
