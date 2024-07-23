package vook.server.api.domain.template_vocabulary.logic;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.template_vocabulary.model.*;
import vook.server.api.globalcommon.annotation.DomainLogic;

import java.util.List;

@DomainLogic
@RequiredArgsConstructor
public class TemplateVocabularyLogic {

    private final TemplateVocabularyRepository vocabularyRepository;
    private final TemplateTermRepository termRepository;

    public List<TemplateTerm> getTermsByType(@Valid TemplateVocabularyType type) {
        TemplateVocabulary vocabulary = vocabularyRepository.findByType(type).orElseThrow();
        return termRepository.findByTemplateVocabulary(vocabulary);
    }
}
