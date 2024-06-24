package vook.server.api.web.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.demo.reqres.SearchTermRequest;
import vook.server.api.web.demo.reqres.SearchTermResponse;

@Tag(name = "demo", description = "VooK 데모용 API")
public interface DemoApi {

    @Operation(summary = "용어 검색")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchApiTermResponse.class)
                    )
            ),
    })
    CommonApiResponse<SearchTermResponse> searchTerm(SearchTermRequest request);

    class SearchApiTermResponse extends CommonApiResponse<SearchTermResponse> {
    }
}
