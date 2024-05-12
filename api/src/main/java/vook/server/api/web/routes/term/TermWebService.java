package vook.server.api.web.routes.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.GlossaryService;
import vook.server.api.model.Glossary;
import vook.server.api.outbound.search.SearchResult;
import vook.server.api.outbound.search.SearchService;

@Service
@Transactional
@RequiredArgsConstructor
public class TermWebService {

    private final GlossaryService glossaryService;
    private final SearchService searchService;

    public SearchResponse search(SearchRequest request) {
        Glossary glossary = glossaryService.findByUid(request.getGlossaryUid()).orElseThrow();
        SearchResult searchResult = searchService.search(request.toSearchParam(glossary));
        return SearchResponse.from(searchResult);
    }
}
