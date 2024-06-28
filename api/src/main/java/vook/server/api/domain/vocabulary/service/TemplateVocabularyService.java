package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.model.*;
import vook.server.api.domain.vocabulary.service.data.TemplateVocabularyCreateCommand;
import vook.server.api.globalcommon.annotation.DomainService;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class TemplateVocabularyService {

    private final TemplateVocabularyRepository vocabularyRepository;
    private final TemplateTermRepository termRepository;

    public void create(@Valid TemplateVocabularyCreateCommand command) {
        TemplateVocabulary vocabulary = vocabularyRepository.save(command.toVocabulary());
        termRepository.saveAll(command.toTerms(vocabulary));
    }

    public List<TemplateTerm> getTermsByName(@Valid TemplateVocabularyName name) {
        TemplateVocabulary vocabulary = vocabularyRepository.findByName(name).orElseThrow();
        return termRepository.findByTemplateVocabulary(vocabulary);
    }
}
