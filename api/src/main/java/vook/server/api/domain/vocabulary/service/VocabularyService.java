package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.exception.VocabularyTermNotFoundException;
import vook.server.api.domain.vocabulary.model.*;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyDeleteCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyTermAddCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyUpdateCommand;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository repository;
    private final VocabularyTermRepository termsRepository;

    public List<Vocabulary> findAllBy(@NotNull UserId userId) {
        return repository.findAllByUserId(userId);
    }

    public Vocabulary create(@Valid VocabularyCreateCommand command) {
        UserId userId = command.getUserId();
        if (repository.findAllByUserId(userId).size() >= 3) {
            throw new VocabularyLimitExceededException();
        }

        return repository.save(Vocabulary.forCreateOf(command.getName(), new UserId(userId.getId())));
    }

    public void update(@Valid VocabularyUpdateCommand command) {
        Vocabulary vocabulary = repository.findByUid(command.getVocabularyUid()).orElseThrow(VocabularyNotFoundException::new);
        vocabulary.update(command.getName());
    }

    public void delete(@Valid VocabularyDeleteCommand command) {
        Vocabulary vocabulary = repository.findByUid(command.getVocabularyUid()).orElseThrow(VocabularyNotFoundException::new);
        repository.delete(vocabulary);
    }

    public Vocabulary getByUid(@NotBlank String vocabularyUid) {
        return repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
    }

    public void addTerm(@Valid VocabularyTermAddCommand command) {
        Vocabulary vocabulary = repository.findByUid(command.getVocabularyUid()).orElseThrow(VocabularyNotFoundException::new);
        int termCount = vocabulary.termCount();
        if (termCount >= 100) {
            throw new TermLimitExceededException();
        }

        vocabulary.addTerm(command.getTermId());
    }

    public Vocabulary getByTermId(@NotNull TermId termId) {
        VocabularyTerm vocabularyTerm = termsRepository.findByTermId(termId).orElseThrow(VocabularyTermNotFoundException::new);
        return vocabularyTerm.getVocabulary();
    }
}
