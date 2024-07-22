package vook.server.api.domain.demo.service;

import com.meilisearch.sdk.model.Searchable;

import java.util.ArrayList;
import java.util.HashMap;

public record DemoTermSearchResult(
        String query,
        int processingTimeMs,
        ArrayList<HashMap<String, Object>> hits
) {

    public static DemoTermSearchResult from(Searchable search) {
        return new DemoTermSearchResult(
                search.getQuery(),
                search.getProcessingTimeMs(),
                search.getHits()
        );
    }
}
