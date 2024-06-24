package vook.server.api.domain.demo.service;

import vook.server.api.domain.demo.service.data.DemoTermSearchParams;
import vook.server.api.domain.demo.service.data.DemoTermSearchResult;

public interface DemoTermSearchService {
    DemoTermSearchResult search(DemoTermSearchParams params);
}
