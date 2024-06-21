package vook.server.api.devhelper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.demo.domain.DemoTerm;
import vook.server.api.app.contexts.demo.domain.DemoTermRepository;
import vook.server.api.app.contexts.demo.domain.DemoTermSynonymRepository;
import vook.server.api.app.contexts.user.domain.SocialUserRepository;
import vook.server.api.app.contexts.user.domain.UserInfoRepository;
import vook.server.api.app.contexts.user.domain.UserRepository;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.infra.search.demo.MeilisearchDemoTermSearchService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final DemoTermRepository demoTermRepository;
    private final DemoTermSynonymRepository demoTermSynonymRepository;
    private final VocabularyRepository vocabularyRepository;
    private final UserInfoRepository userInfoRepository;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;
    private final MeilisearchDemoTermSearchService searchService;
    private final TestTermsLoader testTermsLoader;

    public void init() {
        deleteAll();

        List<DemoTerm> devTerms = testTermsLoader.getTerms("classpath:init/개발.tsv");
        demoTermRepository.saveAll(devTerms);

        searchService.init();
        searchService.addTerms(devTerms);
    }

    private void deleteAll() {
        // 데모 용어
        demoTermSynonymRepository.deleteAllInBatch();
        demoTermRepository.deleteAllInBatch();

        // 용어집
        vocabularyRepository.deleteAllInBatch();

        // 사용자
        userInfoRepository.deleteAllInBatch();
        socialUserRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // 검색 엔진
        searchService.clearAll();
    }
}
