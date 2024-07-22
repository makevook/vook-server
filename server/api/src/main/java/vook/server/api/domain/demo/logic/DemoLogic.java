package vook.server.api.domain.demo.logic;

import lombok.RequiredArgsConstructor;
import vook.server.api.globalcommon.annotation.DomainLogic;

@DomainLogic
@RequiredArgsConstructor
public class DemoLogic {

    private final DemoTermSearchService searchService;

    public DemoTermSearchResult searchTerm(DemoTermSearchCommand params) {
        return searchService.search(params);
    }
}
