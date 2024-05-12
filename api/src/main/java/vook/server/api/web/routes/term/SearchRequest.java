package vook.server.api.web.routes.term;

import lombok.Data;
import vook.server.api.model.Glossary;
import vook.server.api.outbound.search.SearchParams;

@Data
public class SearchRequest {

    private String glossaryUid;
    private String query;

    private boolean withFormat;
    private String highlightPreTag;
    private String highlightPostTag;

    public SearchParams toSearchParam(Glossary glossary) {
        return SearchParams.builder()
                .glossary(glossary)
                .query(query)
                .withFormat(withFormat)
                .highlightPreTag(highlightPreTag)
                .highlightPostTag(highlightPostTag)
                .build();
    }
}
