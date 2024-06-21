package vook.server.api.app.contexts.demo.application.data;

import com.meilisearch.sdk.model.Searchable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class DemoTermSearchResult {

    private String query;
    private int processingTimeMs;
    private ArrayList<HashMap<String, Object>> hits;

    public static DemoTermSearchResult from(Searchable search) {
        DemoTermSearchResult demoTermSearchResult = new DemoTermSearchResult();
        demoTermSearchResult.query = search.getQuery();
        demoTermSearchResult.processingTimeMs = search.getProcessingTimeMs();
        demoTermSearchResult.hits = search.getHits();
        return demoTermSearchResult;
    }
}
