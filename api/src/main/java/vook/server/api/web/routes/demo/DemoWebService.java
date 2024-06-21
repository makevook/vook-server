package vook.server.api.web.routes.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.infra.search.demo.DemoTermSearchResult;
import vook.server.api.app.infra.search.demo.DemoTermSearchService;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class DemoWebService {

    private final DemoTermSearchService searchService;

    public SearchTermResponse searchTerm(SearchTermRequest request) {
        DemoTermSearchResult searchResult = searchService.search(request.toSearchParam());
        return SearchTermResponse.from(searchResult);
    }
}
