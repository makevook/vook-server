package vook.server.api.web.routes.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.GlossaryService;
import vook.server.api.app.TermService;
import vook.server.api.model.Glossary;
import vook.server.api.model.Term;
import vook.server.api.outbound.search.SearchResult;
import vook.server.api.outbound.search.SearchService;
import vook.server.api.web.routes.demo.reqres.RetrieveGlossariesResponse;
import vook.server.api.web.routes.demo.reqres.RetrieveTermsResponse;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DemoWebService {

    private final GlossaryService glossaryService;
    private final TermService termService;
    private final SearchService searchService;

    public List<RetrieveGlossariesResponse> retrieveGlossaries() {
        List<Glossary> glossaries = glossaryService.findAll();
        return RetrieveGlossariesResponse.from(glossaries);
    }

    public List<RetrieveTermsResponse> retrieveTerms(String glossaryUid, Pageable pageable) {
        Glossary glossary = glossaryService.findByUid(glossaryUid).orElseThrow();
        List<Term> terms = termService.findAllBy(glossary, pageable);
        return RetrieveTermsResponse.from(glossary, terms);
    }

    public SearchTermResponse searchTerm(String glossaryUid, SearchTermRequest request) {
        Glossary glossary = glossaryService.findByUid(glossaryUid).orElseThrow();
        SearchResult searchResult = searchService.search(request.toSearchParam(glossary));
        return SearchTermResponse.from(searchResult);
    }
}
