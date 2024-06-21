package vook.server.api.app.contexts.vocabulary.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUserId(UserId userId);

    Optional<Vocabulary> findByUid(String vocabularyUid);
}
