package vook.server.api.web.routes.term;

import lombok.Data;
import vook.server.api.model.Glossary;
import vook.server.api.outbound.search.SearchParams;

@Data
public class SearchRequest {

    private String glossaryUid;
    private String query;

    public SearchParams toSearchParam(Glossary glossary) {
        return SearchParams.of(glossary, query);
    }
}
