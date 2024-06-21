package vook.server.api.devhelper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.context.demo.model.DemoTerm;
import vook.server.api.app.context.demo.model.DemoTermRepository;
import vook.server.api.app.context.demo.model.DemoTermSynonymRepository;
import vook.server.api.app.context.user.model.SocialUserRepository;
import vook.server.api.app.context.user.model.UserInfoRepository;
import vook.server.api.app.context.user.model.UserRepository;
import vook.server.api.app.context.vocabulary.model.VocabularyRepository;
import vook.server.api.app.infra.search.demo.DemoTermSearchService;

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
    private final DemoTermSearchService searchService;
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
