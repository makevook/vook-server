package vook.server.api.devhelper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.demo.model.DemoTerm;
import vook.server.api.domain.demo.model.DemoTermRepository;
import vook.server.api.domain.demo.model.DemoTermSynonymRepository;
import vook.server.api.domain.template_vocabulary.logic.TemplateVocabularyLogic;
import vook.server.api.domain.template_vocabulary.logic.dto.TemplateVocabularyCreateCommand;
import vook.server.api.domain.template_vocabulary.model.TemplateTermRepository;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyRepository;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyType;
import vook.server.api.domain.user.model.social_user.SocialUserRepository;
import vook.server.api.domain.user.model.user.UserRepository;
import vook.server.api.domain.user.model.user_info.UserInfoRepository;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.infra.search.demo.MeilisearchDemoTermSearchService;
import vook.server.api.infra.vocabulary.cache.UserVocabularyCacheRepository;
import vook.server.api.infra.vocabulary.jpa.VocabularyJpaRepository;

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
    private final VocabularyJpaRepository vocabularyJpaRepository;
    private final UserVocabularyCacheRepository userVocabularyCacheRepository;
    private final UserInfoRepository userInfoRepository;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;

    private final TestTermsLoader testTermsLoader;
    private final MeilisearchDemoTermSearchService searchService;

    private final TemplateVocabularyLogic templateVocabularyLogic;

    public void init() {
        deleteAll();

        // 데모 용어집
        List<DemoTerm> demoTerms = testTermsLoader.getTerms(
                "classpath:init/데모.tsv",
                InitService::convertToDemoTerm
        );
        demoTermRepository.saveAll(demoTerms);

        searchService.init();
        searchService.addTerms(demoTerms);

        // 템플릿 용어집
        createTemplateVocabulary(TemplateVocabularyType.DEVELOPMENT, "classpath:init/템플릿용어집-개발.tsv");
        createTemplateVocabulary(TemplateVocabularyType.MARKETING, "classpath:init/템플릿용어집-마케팅.tsv");
        createTemplateVocabulary(TemplateVocabularyType.DESIGN, "classpath:init/템플릿용어집-디자인.tsv");
        createTemplateVocabulary(TemplateVocabularyType.GENERAL_OFFICE, "classpath:init/템플릿용어집-일반사무.tsv");
    }

    private void createTemplateVocabulary(TemplateVocabularyType type, String location) {
        templateVocabularyLogic.create(new TemplateVocabularyCreateCommand(
                type,
                testTermsLoader.getTerms(location, InitService::convertToTemplateTerm)
        ));
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
        vocabularyJpaRepository.deleteAllInBatch();
        userVocabularyCacheRepository.deleteAll();

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

    public static List<TemplateVocabularyCreateCommand.Term> convertToTemplateTerm(List<TestTermsLoader.RawTerm> input) {
        return input.stream()
                .map(t -> new TemplateVocabularyCreateCommand.Term(
                        t.getTerm(),
                        t.getMeaning(),
                        Arrays.stream(t.getSynonyms().split(","))
                                .map(String::trim)
                                .toList()
                ))
                .toList();
    }
}
