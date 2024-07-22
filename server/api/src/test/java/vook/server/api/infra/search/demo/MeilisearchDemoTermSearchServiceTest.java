package vook.server.api.infra.search.demo;

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
import vook.server.api.domain.demo.service.DemoTermSearchCommand;
import vook.server.api.domain.demo.service.DemoTermSearchResult;
import vook.server.api.testhelper.IntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MeilisearchDemoTermSearchServiceTest extends IntegrationTestBase {

    @Autowired
    MeilisearchDemoTermSearchService service;

    @Autowired
    private TestTermsLoader testTermsLoader;
    @Autowired
    private DemoTermRepository demoTermRepository;

    @BeforeAll
    void beforeAll() {
        List<DemoTerm> terms = testTermsLoader.getTerms(
                "classpath:init/demo.tsv",
                InitService::convertToDemoTerm
        );
        demoTermRepository.saveAll(terms);
        service.init();
        service.addTerms(terms);
    }

    @AfterAll
    void afterAll() {
        service.clearAll();
    }

    @Test
    void search() {
        DemoTermSearchCommand params = DemoTermSearchCommand.builder()
                .query("하이브리드앱")
                .withFormat(false)
                .highlightPreTag("<em>")
                .highlightPostTag("</em>")
                .build();

        DemoTermSearchResult result = service.search(params);

        assertThat(result).isNotNull();
        assertThat(result.query()).isEqualTo("하이브리드앱");
        assertThat(result.hits()).isNotEmpty();
    }
}
