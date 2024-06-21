package vook.server.api.app.contexts.demo.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoTermSearchService searchService;

    public DemoTermSearchResult searchTerm(DemoTermSearchParams params) {
        return searchService.search(params);
    }
}
