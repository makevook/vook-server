package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.demo.repo.DemoTermRepository;
import vook.server.api.app.demo.repo.DemoTermSynonymRepository;
import vook.server.api.app.user.repo.SocialUserRepository;
import vook.server.api.app.user.repo.UserInfoRepository;
import vook.server.api.app.user.repo.UserRepository;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.outbound.search.DemoTermSearchService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final ResourceLoader resourceLoader;

    private final DemoTermRepository demoTermRepository;
    private final DemoTermSynonymRepository demoTermSynonymRepository;
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

        // 사용자
        userInfoRepository.deleteAllInBatch();
        socialUserRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // 검색 엔진
        searchService.clearAll();
    }
}
