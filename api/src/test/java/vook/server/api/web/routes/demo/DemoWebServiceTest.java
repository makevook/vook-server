package vook.server.api.web.routes.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.domain.demo.model.DemoTermRepository;
import vook.server.api.app.infra.search.demo.DemoTermSearchService;
import vook.server.api.devhelper.app.TestTermsLoader;
import vook.server.api.app.domain.demo.model.DemoTerm;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoWebServiceTest extends IntegrationTestBase {

    @Autowired
    private DemoWebService demoWebService;

    @Autowired
    private TestTermsLoader testTermsLoader;
    @Autowired
    private DemoTermRepository demoTermRepository;
    @Autowired
    private DemoTermSearchService demoTermSearchService;

    @BeforeAll
    void beforeAll() {
        List<DemoTerm> terms = testTermsLoader.getTerms("classpath:init/개발.tsv");
        demoTermRepository.saveAll(terms);
        demoTermSearchService.init();
        demoTermSearchService.addTerms(terms);
    }

    @AfterAll
    void afterAll() {
        demoTermSearchService.clearAll();
    }

    @Test
    void searchTerm() {
        SearchTermRequest searchTermRequest = new SearchTermRequest();
        searchTermRequest.setQuery("하이브리드앱");
        searchTermRequest.setWithFormat(false);
        searchTermRequest.setHighlightPreTag("<em>");
        searchTermRequest.setHighlightPostTag("</em>");

        SearchTermResponse searchTermResponse = demoWebService.searchTerm(searchTermRequest);

        assertThat(searchTermResponse).isNotNull();
        assertThat(searchTermResponse.getQuery()).isEqualTo("하이브리드앱");
        assertThat(searchTermResponse.getHits()).isNotEmpty();
    }
}
