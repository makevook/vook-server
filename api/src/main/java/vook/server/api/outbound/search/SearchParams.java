package vook.server.api.outbound.search;

import lombok.Getter;
import vook.server.api.model.Glossary;

@Getter
public class SearchParams {
    
    private Glossary glossary;
    private String query;

    public static SearchParams of(Glossary glossary, String query) {
        SearchParams searchParams = new SearchParams();
        searchParams.glossary = glossary;
        searchParams.query = query;
        return searchParams;
    }
}
