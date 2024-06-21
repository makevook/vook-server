package vook.server.api.app.contexts.vocabulary.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyCreateCommand;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyDeleteCommand;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyUpdateCommand;
import vook.server.api.app.contexts.vocabulary.domain.UserId;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyNotFoundException;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository repository;

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
        Vocabulary vocabulary = validateAndGetVocabulary(command.getVocabularyUid(), command.getUserId());
        vocabulary.update(command.getName());
    }

    public void delete(@Valid VocabularyDeleteCommand command) {
        Vocabulary vocabulary = validateAndGetVocabulary(command.getVocabularyUid(), command.getUserId());
        repository.delete(vocabulary);
    }

    public Vocabulary findByUidAndUser(@NotBlank String vocabularyUid, @NotNull UserId userId) {
        return validateAndGetVocabulary(vocabularyUid, userId);
    }

    private Vocabulary validateAndGetVocabulary(@NotBlank String vocabularyUid, @NotNull UserId userId) {
        Vocabulary vocabulary = repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
        if (!vocabulary.isValidOwner(userId)) {
            throw new VocabularyNotFoundException();
        }
        return vocabulary;
    }
}
