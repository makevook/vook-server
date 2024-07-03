package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyUpdateCommand;
import vook.server.api.globalcommon.annotation.DomainService;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository repository;
    private final SearchManagementService searchService;

    public List<Vocabulary> findAllBy(@NotNull UserId userId) {
        return repository.findAllByUserId(userId);
    }

    public Vocabulary create(@Valid VocabularyCreateCommand command) {
        UserId userId = command.userId();
        if (repository.findAllByUserId(userId).size() >= 3) {
            throw new VocabularyLimitExceededException();
        }

        Vocabulary saved = repository.save(Vocabulary.forCreateOf(command.name(), new UserId(userId.getId())));
        searchService.save(saved);
        return saved;
    }

    public void update(@Valid VocabularyUpdateCommand command) {
        Vocabulary vocabulary = repository.findByUid(command.vocabularyUid()).orElseThrow(VocabularyNotFoundException::new);
        vocabulary.update(command.name());
    }

    public void delete(@NotBlank String vocabularyUid) {
        Vocabulary vocabulary = repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
        repository.delete(vocabulary);
        searchService.delete(vocabulary);
    }

    public Vocabulary getByUid(@NotBlank String vocabularyUid) {
        return repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
    }

    public interface SearchManagementService {
        void save(Vocabulary saved);

        void delete(Vocabulary vocabulary);
    }
}
