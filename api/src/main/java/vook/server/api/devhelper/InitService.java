package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.demo.repo.DemoTermRepository;
import vook.server.api.app.demo.repo.DemoTermSynonymRepository;
import vook.server.api.app.terms.repo.TermsRepository;
import vook.server.api.app.user.repo.SocialUserRepository;
import vook.server.api.app.user.repo.UserInfoRepository;
import vook.server.api.app.user.repo.UserRepository;
import vook.server.api.app.user.repo.UserTermsAgreeRepository;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.model.terms.Terms;
import vook.server.api.outbound.search.DemoTermSearchService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final ResourceLoader resourceLoader;

    private final DemoTermRepository demoTermRepository;
    private final DemoTermSynonymRepository demoTermSynonymRepository;
    private final UserTermsAgreeRepository userTermsAgreeRepository;
    private final UserInfoRepository userInfoRepository;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final DemoTermSearchService searchService;
    private final TestTermsLoader testTermsLoader;

    public void init() {
        deleteAll();

        termsRepository.save(Terms.of("이용약관", loadContents("classpath:init/이용약관.txt"), true));
        termsRepository.save(Terms.of("개인정보 수집 이용 약관", loadContents("classpath:init/개인정보_수집_이용_약관.txt"), true));
        termsRepository.save(Terms.of("마케팅 메일 수신 약관", loadContents("classpath:init/마케팅_메일_수신_약관.txt"), false));

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
        userTermsAgreeRepository.deleteAllInBatch();
        userInfoRepository.deleteAllInBatch();
        socialUserRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // 약관
        termsRepository.deleteAllInBatch();

        // 검색 엔진
        searchService.clearAll();
    }

    private String loadContents(String location) {
        try {
            InputStream inputStream = resourceLoader.getResource(location).getInputStream();
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
