package vook.server.api.domain.template_vocabulary.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.template_vocabulary.model.TemplateTerm;
import vook.server.api.domain.template_vocabulary.model.TemplateTermRepository;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyRepository;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyType;
import vook.server.api.testhelper.IntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class TemplateVocabularyLogicTest extends IntegrationTestBase {

    @Autowired
    TemplateVocabularyLogic service;

    @Autowired
    TemplateVocabularyRepository vocabularyRepository;
    @Autowired
    TemplateTermRepository termRepository;

    @Test
    @DisplayName("템플릿 용어집 내 용어 조회 - 정상")
    void getTermsByType() {
        // when
        List<TemplateTerm> terms = service.getTermsByType(TemplateVocabularyType.DEVELOPMENT);

        // then
        assertThat(terms).isNotEmpty();
    }
}
