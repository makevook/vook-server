package vook.server.api.domain.vocabulary.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.vocabulary.logic.dto.TemplateVocabularyCreateCommand;
import vook.server.api.domain.vocabulary.model.*;
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
    @DisplayName("템플릿 용어집 생성 - 정상")
    void create() {
        // given
        TemplateVocabularyCreateCommand command = new TemplateVocabularyCreateCommand(
                TemplateVocabularyName.DEVELOPMENT,
                List.of(
                        new TemplateVocabularyCreateCommand.Term("term1", "meaning1", List.of("synonym1")),
                        new TemplateVocabularyCreateCommand.Term("term2", "meaning2", List.of("synonym2"))
                )
        );

        // when
        service.create(command);

        // then
        List<TemplateVocabulary> vocabularies = vocabularyRepository.findAll();
        assertThat(vocabularies).hasSize(1);

        TemplateVocabulary vocabulary = vocabularies.getFirst();
        assertThat(vocabulary.getId()).isNotNull();
        assertThat(vocabulary.getName()).isEqualTo(TemplateVocabularyName.DEVELOPMENT);

        List<TemplateTerm> terms = termRepository.findByTemplateVocabulary(vocabulary);
        assertThat(terms).hasSize(2);
        assertThat(terms.get(0).getTerm()).isEqualTo("term1");
        assertThat(terms.get(0).getMeaning()).isEqualTo("meaning1");
        assertThat(terms.get(0).getSynonyms()).isEqualTo(List.of("synonym1"));
        assertThat(terms.get(1).getTerm()).isEqualTo("term2");
        assertThat(terms.get(1).getMeaning()).isEqualTo("meaning2");
        assertThat(terms.get(1).getSynonyms()).isEqualTo(List.of("synonym2"));
    }

    @Test
    @DisplayName("템플릿 용어집 내 용어 조회 - 정상")
    void getTermsByName() {
        // given
        TemplateVocabularyCreateCommand command = new TemplateVocabularyCreateCommand(
                TemplateVocabularyName.DEVELOPMENT,
                List.of(
                        new TemplateVocabularyCreateCommand.Term("term1", "meaning1", List.of("synonym1", "synonym2")),
                        new TemplateVocabularyCreateCommand.Term("term2", "meaning2", List.of("synonym3", "synonym4"))
                )
        );
        service.create(command);

        // when
        List<TemplateTerm> terms = service.getTermsByName(TemplateVocabularyName.DEVELOPMENT);

        // then
        assertThat(terms).hasSize(2);
        assertThat(terms.get(0).getTerm()).isEqualTo("term1");
        assertThat(terms.get(0).getMeaning()).isEqualTo("meaning1");
        assertThat(terms.get(0).getSynonyms()).isEqualTo(List.of("synonym1", "synonym2"));
        assertThat(terms.get(1).getTerm()).isEqualTo("term2");
        assertThat(terms.get(1).getMeaning()).isEqualTo("meaning2");
        assertThat(terms.get(1).getSynonyms()).isEqualTo(List.of("synonym3", "synonym4"));
    }

}
