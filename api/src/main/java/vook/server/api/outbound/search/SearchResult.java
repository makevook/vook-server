package vook.server.api.outbound.search;

import com.meilisearch.sdk.model.Searchable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class SearchResult {

    private String query;
    private int processingTimeMs;
    private ArrayList<HashMap<String, Object>> hits;

    public static SearchResult from(Searchable search) {
        SearchResult searchResult = new SearchResult();
        searchResult.query = search.getQuery();
        searchResult.processingTimeMs = search.getProcessingTimeMs();
        searchResult.hits = search.getHits();
        return searchResult;
    }
}
