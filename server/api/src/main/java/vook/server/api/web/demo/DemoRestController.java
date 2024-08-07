package vook.server.api.web.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.domain.demo.service.DemoTermSearchResult;
import vook.server.api.domain.demo.service.DemoTermSearchService;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.demo.reqres.SearchTermRequest;
import vook.server.api.web.demo.reqres.SearchTermResponse;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoRestController implements DemoApi {

    private final DemoTermSearchService service;

    @Override
    @PostMapping("/terms/search")
    public CommonApiResponse<SearchTermResponse> searchTerm(
            @RequestBody SearchTermRequest request
    ) {
        DemoTermSearchResult searchResult = service.search(request.toSearchParam());
        SearchTermResponse result = SearchTermResponse.from(searchResult);
        return CommonApiResponse.okWithResult(result);
    }
}
