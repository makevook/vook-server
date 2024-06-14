package vook.server.api.app.domain.vocabulary.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUser(User user);

    Optional<Vocabulary> findByUid(String vocabularyUid);
}
