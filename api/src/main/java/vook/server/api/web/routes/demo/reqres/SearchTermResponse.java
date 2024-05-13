package vook.server.api.web.routes.demo.reqres;

import lombok.Getter;
import vook.server.api.outbound.search.SearchResult;

import java.util.List;
import java.util.Map;

@Getter
public class SearchTermResponse {

    private String query;
    private List<Document> hits;

    public static SearchTermResponse from(SearchResult searchResult) {
        SearchTermResponse searchTermResponse = new SearchTermResponse();
        searchTermResponse.query = searchResult.getQuery();
        searchTermResponse.hits = searchResult.getHits().stream().map(document -> {
            Object formatted = document.get("_formatted");
            if (formatted instanceof Map formattedDocument) {
                return Document.from(formattedDocument);
            } else {
                return Document.from(document);
            }
        }).toList();
        return searchTermResponse;
    }

    @Getter
    public static class Document {
        private String term;
        private String synonyms;
        private String meaning;

        public static Document from(Map<String, Object> document) {
            Document result = new Document();
            result.term = (String) document.get("term");
            result.synonyms = (String) document.get("synonyms");
            result.meaning = (String) document.get("meaning");
            return result;
        }
    }
}
