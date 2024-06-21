package vook.server.api.app.contexts.vocabulary.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyCreateCommand;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyDeleteCommand;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyUpdateCommand;
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

    public List<Vocabulary> findAllBy(@NotNull User user) {
        return repository.findAllByUser(user);
    }

    public Vocabulary create(@Valid VocabularyCreateCommand command) {
        User user = command.getUser();
        if (repository.findAllByUser(user).size() >= 3) {
            throw new VocabularyLimitExceededException();
        }

        return repository.save(Vocabulary.forCreateOf(command.getName(), user));
    }

    public void update(@Valid VocabularyUpdateCommand command) {
        Vocabulary vocabulary = validateAndGetVocabulary(command.getVocabularyUid(), command.getUser());
        vocabulary.update(command.getName());
    }

    public void delete(@Valid VocabularyDeleteCommand command) {
        Vocabulary vocabulary = validateAndGetVocabulary(command.getVocabularyUid(), command.getUser());
        repository.delete(vocabulary);
    }

    public Vocabulary findByUidAndUser(@NotBlank String vocabularyUid, @NotNull User user) {
        return validateAndGetVocabulary(vocabularyUid, user);
    }

    private Vocabulary validateAndGetVocabulary(@NotBlank String vocabularyUid, @NotNull User user) {
        Vocabulary vocabulary = repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
        if (!vocabulary.isValidOwner(user)) {
            throw new VocabularyNotFoundException();
        }
        return vocabulary;
    }
}
