package vook.server.api.domain.demo.logic;

import vook.server.api.domain.demo.logic.dto.DemoTermSearchCommand;
import vook.server.api.domain.demo.logic.dto.DemoTermSearchResult;

public interface DemoTermSearchService {
    DemoTermSearchResult search(DemoTermSearchCommand params);
}
