package vook.server.api.infra.vocabulary.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;
import java.util.Optional;

public interface VocabularyJpaRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUserUid(UserUid userUid);

    Optional<Vocabulary> findByUid(String vocabularyUid);
}
