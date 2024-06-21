package vook.server.api.app.contexts.demo.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.contexts.demo.application.data.DemoTermSearchParams;
import vook.server.api.app.contexts.demo.application.data.DemoTermSearchResult;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoTermSearchService searchService;

    public DemoTermSearchResult searchTerm(DemoTermSearchParams params) {
        return searchService.search(params);
    }
}
