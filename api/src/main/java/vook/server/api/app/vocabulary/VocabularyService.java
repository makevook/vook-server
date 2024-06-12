package vook.server.api.app.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.vocabulary.data.VocabularyCreateCommand;
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
        Vocabulary vocabulary = repository.findByUid(command.getVocabularyUid()).orElseThrow(VocabularyNotFoundException::new);
        if (!vocabulary.isValidOwner(command.getUser())) {
            throw new VocabularyNotFoundException();
        }

        vocabulary.update(command.getName());
    }
}
