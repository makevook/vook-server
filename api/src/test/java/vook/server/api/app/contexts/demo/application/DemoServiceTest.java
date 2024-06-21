package vook.server.api.app.contexts.demo.application;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.demo.application.data.DemoTermSearchParams;
import vook.server.api.app.contexts.demo.application.data.DemoTermSearchResult;
import vook.server.api.app.contexts.demo.domain.DemoTerm;
import vook.server.api.app.contexts.demo.domain.DemoTermRepository;
import vook.server.api.app.infra.search.demo.MeilisearchDemoTermSearchService;
import vook.server.api.devhelper.app.TestTermsLoader;
import vook.server.api.testhelper.IntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoServiceTest extends IntegrationTestBase {
    @Autowired
    private DemoService demoService;

    @Autowired
    private TestTermsLoader testTermsLoader;
    @Autowired
    private DemoTermRepository demoTermRepository;
    @Autowired
    private MeilisearchDemoTermSearchService demoTermSearchService;

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
        DemoTermSearchParams params = DemoTermSearchParams.builder()
                .query("하이브리드앱")
                .withFormat(false)
                .highlightPreTag("<em>")
                .highlightPostTag("</em>")
                .build();

        DemoTermSearchResult result = demoService.searchTerm(params);

        assertThat(result).isNotNull();
        assertThat(result.getQuery()).isEqualTo("하이브리드앱");
        assertThat(result.getHits()).isNotEmpty();
    }
}
