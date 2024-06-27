package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.vocabulary.model.*;
import vook.server.api.domain.vocabulary.service.data.TemplateVocabularyCreateCommand;

import java.util.List;

@Service
@Validated
@Transactional
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
