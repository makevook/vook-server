package vook.server.api.devhelper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.demo.model.DemoTerm;
import vook.server.api.domain.demo.model.DemoTermRepository;
import vook.server.api.domain.demo.model.DemoTermSynonymRepository;
import vook.server.api.domain.user.model.SocialUserRepository;
import vook.server.api.domain.user.model.UserInfoRepository;
import vook.server.api.domain.user.model.UserRepository;
import vook.server.api.domain.vocabulary.model.TemplateTermRepository;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyRepository;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.infra.search.demo.MeilisearchDemoTermSearchService;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final DemoTermRepository demoTermRepository;
    private final DemoTermSynonymRepository demoTermSynonymRepository;
    private final TemplateTermRepository templateTermRepository;
    private final TemplateVocabularyRepository templateVocabularyRepository;
    private final TermRepository termRepository;
    private final VocabularyRepository vocabularyRepository;
    private final UserInfoRepository userInfoRepository;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;

    private final TestTermsLoader testTermsLoader;
    private final MeilisearchDemoTermSearchService searchService;

    public void init() {
        deleteAll();

        List<DemoTerm> devTerms = testTermsLoader.getTerms(
                "classpath:init/데모.tsv",
                InitService::convertToDemoTerm
        );
        demoTermRepository.saveAll(devTerms);

        searchService.init();
        searchService.addTerms(devTerms);
    }

    private void deleteAll() {
        // 데모 용어
        demoTermSynonymRepository.deleteAllInBatch();
        demoTermRepository.deleteAllInBatch();

        // 템플릿 용어집
        templateTermRepository.deleteAllInBatch();
        templateVocabularyRepository.deleteAllInBatch();

        // 용어집
        termRepository.deleteAllInBatch();
        vocabularyRepository.deleteAllInBatch();

        // 사용자
        userInfoRepository.deleteAllInBatch();
        socialUserRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // 검색 엔진
        searchService.clearAll();
    }

    public static List<DemoTerm> convertToDemoTerm(List<TestTermsLoader.RawTerm> input) {
        return input.stream()
                .map(t -> {
                    DemoTerm term = DemoTerm.forCreateOf(t.getTerm(), t.getMeaning());
                    String[] synonymArray = t.getSynonyms().split("//n");
                    Arrays.stream(synonymArray)
                            .map(String::trim)
                            .forEach(term::addSynonym);
                    return term;
                })
                .toList();
    }
}
