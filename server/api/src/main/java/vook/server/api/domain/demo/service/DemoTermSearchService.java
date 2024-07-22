package vook.server.api.domain.demo.service;

import vook.server.api.domain.demo.logic.DemoTermSearchCommand;
import vook.server.api.domain.demo.logic.DemoTermSearchResult;

public interface DemoTermSearchService {
    DemoTermSearchResult search(DemoTermSearchCommand params);
}
