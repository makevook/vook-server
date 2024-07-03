package vook.server.api.web.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.common.swagger.annotation.IncludeOkResponse;
import vook.server.api.web.demo.reqres.SearchTermRequest;
import vook.server.api.web.demo.reqres.SearchTermResponse;

@Tag(name = "demo", description = "VooK 데모용 API")
public interface DemoApi {

    @Operation(summary = "용어 검색")
    @IncludeOkResponse(implementation = SearchApiTermResponse.class)
    CommonApiResponse<SearchTermResponse> searchTerm(SearchTermRequest request);

    class SearchApiTermResponse extends CommonApiResponse<SearchTermResponse> {
    }
}
