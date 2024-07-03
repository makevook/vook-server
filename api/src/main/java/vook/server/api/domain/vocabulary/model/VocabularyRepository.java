package vook.server.api.domain.vocabulary.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUserUid(UserUid userUid);

    Optional<Vocabulary> findByUid(String vocabularyUid);
}
