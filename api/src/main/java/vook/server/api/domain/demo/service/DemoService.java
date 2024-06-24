package vook.server.api.domain.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.domain.demo.service.data.DemoTermSearchParams;
import vook.server.api.domain.demo.service.data.DemoTermSearchResult;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoTermSearchService searchService;

    public DemoTermSearchResult searchTerm(DemoTermSearchParams params) {
        return searchService.search(params);
    }
}
