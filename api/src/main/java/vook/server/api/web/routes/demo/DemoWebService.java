package vook.server.api.web.routes.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.DemoService;
import vook.server.api.model.demo.DemoGlossary;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.outbound.search.DemoTermSearchResult;
import vook.server.api.outbound.search.DemoTermSearchService;
import vook.server.api.web.routes.demo.reqres.RetrieveGlossariesResponse;
import vook.server.api.web.routes.demo.reqres.RetrieveTermsResponse;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DemoWebService {

    private final DemoService demoService;
    private final DemoTermSearchService searchService;

    public List<RetrieveGlossariesResponse> retrieveGlossaries() {
        List<DemoGlossary> glossaries = demoService.findAllDemoGlossary();
        return RetrieveGlossariesResponse.from(glossaries);
    }

    public List<RetrieveTermsResponse> retrieveTerms(String glossaryUid, Pageable pageable) {
        DemoGlossary glossary = demoService.findDemoGlossaryByUid(glossaryUid).orElseThrow();
        List<DemoTerm> terms = demoService.findAllDemoTermBy(glossary, pageable);
        return RetrieveTermsResponse.from(terms);
    }

    public SearchTermResponse searchTerm(String glossaryUid, SearchTermRequest request) {
        DemoGlossary glossary = demoService.findDemoGlossaryByUid(glossaryUid).orElseThrow();
        DemoTermSearchResult searchResult = searchService.search(request.toSearchParam(glossary));
        return SearchTermResponse.from(searchResult);
    }
}
