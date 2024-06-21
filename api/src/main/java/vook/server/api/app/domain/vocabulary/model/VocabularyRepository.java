package vook.server.api.app.domain.vocabulary.model;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.app.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUser(User user);

    Optional<Vocabulary> findByUid(String vocabularyUid);
}
