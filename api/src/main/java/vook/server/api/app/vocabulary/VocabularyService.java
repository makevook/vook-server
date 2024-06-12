package vook.server.api.app.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.vocabulary.data.VocabularyCreateCommand;
import vook.server.api.app.vocabulary.data.VocabularyDeleteCommand;
import vook.server.api.app.vocabulary.data.VocabularyUpdateCommand;
import vook.server.api.app.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.app.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.app.vocabulary.repo.VocabularyRepository;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;

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

    private Vocabulary validateAndGetVocabulary(String vocabularyUid, User user) {
        Vocabulary vocabulary = repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
        if (!vocabulary.isValidOwner(user)) {
            throw new VocabularyNotFoundException();
        }
        return vocabulary;
    }
}
