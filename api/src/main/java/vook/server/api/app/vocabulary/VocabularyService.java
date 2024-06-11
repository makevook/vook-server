package vook.server.api.app.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}
