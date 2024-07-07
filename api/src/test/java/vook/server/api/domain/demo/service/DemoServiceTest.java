package vook.server.api.domain.demo.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.devhelper.app.InitService;
import vook.server.api.devhelper.app.TestTermsLoader;
import vook.server.api.domain.demo.model.DemoTerm;
import vook.server.api.domain.demo.model.DemoTermRepository;
import vook.server.api.domain.demo.service.data.DemoTermSearchParams;
import vook.server.api.domain.demo.service.data.DemoTermSearchResult;
import vook.server.api.infra.search.demo.MeilisearchDemoTermSearchService;
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
        List<DemoTerm> terms = testTermsLoader.getTerms(
                "classpath:init/데모.tsv",
                InitService::convertToDemoTerm
        );
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
        assertThat(result.query()).isEqualTo("하이브리드앱");
        assertThat(result.hits()).isNotEmpty();
    }
}
