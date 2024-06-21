package vook.server.api.app.contexts.vocabulary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyCreateCommand;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyDeleteCommand;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyUpdateCommand;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository repository;

    public List<Vocabulary> findAllBy(User user) {
        return repository.findAllByUser(user);
    }

    public Vocabulary create(VocabularyCreateCommand command) {
        User user = command.getUser();
        if (repository.findAllByUser(user).size() >= 3) {
            throw new VocabularyLimitExceededException();
        }

        return repository.save(Vocabulary.forCreateOf(command.getName(), user));
    }

    public void update(VocabularyUpdateCommand command) {
        Vocabulary vocabulary = validateAndGetVocabulary(command.getVocabularyUid(), command.getUser());
        vocabulary.update(command.getName());
    }

    public void delete(VocabularyDeleteCommand command) {
        Vocabulary vocabulary = validateAndGetVocabulary(command.getVocabularyUid(), command.getUser());
        repository.delete(vocabulary);
    }

    public Vocabulary findByUidAndUser(String vocabularyUid, User user) {
        return validateAndGetVocabulary(vocabularyUid, user);
    }

    private Vocabulary validateAndGetVocabulary(String vocabularyUid, User user) {
        Vocabulary vocabulary = repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
        if (!vocabulary.isValidOwner(user)) {
            throw new VocabularyNotFoundException();
        }
        return vocabulary;
    }
}
