package vook.server.api.domain.demo.service;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.demo.service.data.DemoTermSearchParams;
import vook.server.api.domain.demo.service.data.DemoTermSearchResult;
import vook.server.api.globalcommon.annotation.DomainService;

@DomainService
@RequiredArgsConstructor
public class DemoService {

    private final DemoTermSearchService searchService;

    public DemoTermSearchResult searchTerm(DemoTermSearchParams params) {
        return searchService.search(params);
    }
}
