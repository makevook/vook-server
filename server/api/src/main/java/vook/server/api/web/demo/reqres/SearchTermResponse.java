package vook.server.api.web.demo.reqres;

import lombok.Builder;
import vook.server.api.domain.demo.logic.DemoTermSearchResult;

import java.util.List;
import java.util.Map;

@Builder
public record SearchTermResponse(
        String query,
        List<Document> hits
) {
    public static SearchTermResponse from(DemoTermSearchResult searchResult) {
        return SearchTermResponse.builder()
                .query(searchResult.query())
                .hits(searchResult.hits().stream().map(document -> {
                    Object formatted = document.get("_formatted");
                    if (formatted instanceof Map formattedDocument) {
                        return Document.from(formattedDocument);
                    } else {
                        return Document.from(document);
                    }
                }).toList())
                .build();
    }

    @Builder
    public record Document(
            String term,
            String synonyms,
            String meaning
    ) {
        public static Document from(Map<String, Object> document) {
            return Document.builder()
                    .term((String) document.get("term"))
                    .synonyms((String) document.get("synonyms"))
                    .meaning((String) document.get("meaning"))
                    .build();
        }
    }
}
