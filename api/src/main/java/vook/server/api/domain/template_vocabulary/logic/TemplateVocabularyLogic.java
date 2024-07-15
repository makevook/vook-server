package vook.server.api.domain.template_vocabulary.logic;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.template_vocabulary.logic.dto.TemplateVocabularyCreateCommand;
import vook.server.api.domain.template_vocabulary.model.*;
import vook.server.api.globalcommon.annotation.DomainLogic;

import java.util.List;

@DomainLogic
@RequiredArgsConstructor
public class TemplateVocabularyLogic {

    private final TemplateVocabularyRepository vocabularyRepository;
    private final TemplateTermRepository termRepository;

    public void create(@Valid TemplateVocabularyCreateCommand command) {
        TemplateVocabulary vocabulary = vocabularyRepository.save(command.toVocabulary());
        termRepository.saveAll(command.toTerms(vocabulary));
    }

    public List<TemplateTerm> getTermsByType(@Valid TemplateVocabularyType type) {
        TemplateVocabulary vocabulary = vocabularyRepository.findByType(type).orElseThrow();
        return termRepository.findByTemplateVocabulary(vocabulary);
    }
}
