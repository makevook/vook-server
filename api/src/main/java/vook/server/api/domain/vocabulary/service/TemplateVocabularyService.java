package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.vocabulary.model.TemplateTermRepository;
import vook.server.api.domain.vocabulary.model.TemplateVocabulary;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyRepository;
import vook.server.api.domain.vocabulary.service.data.TemplateVocabularyCreateCommand;

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
}
