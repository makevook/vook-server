package vook.server.api.app.contexts.demo.application;

import vook.server.api.app.contexts.demo.application.data.DemoTermSearchParams;
import vook.server.api.app.contexts.demo.application.data.DemoTermSearchResult;

public interface DemoTermSearchService {
    DemoTermSearchResult search(DemoTermSearchParams params);
}
