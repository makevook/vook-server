package vook.server.api.web.routes.term;

import lombok.Getter;
import vook.server.api.outbound.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class SearchResponse {

    private String query;
    private ArrayList<HashMap<String, Object>> hits;

    public static SearchResponse from(SearchResult searchResult) {
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.query = searchResult.getQuery();
        searchResponse.hits = searchResult.getHits();
        return searchResponse;
    }
}
