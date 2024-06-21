package vook.server.api.app.contexts.vocabulary.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.app.contexts.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUser(User user);

    Optional<Vocabulary> findByUid(String vocabularyUid);
}
