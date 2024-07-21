package vook.server.api.domain.vocabulary.model.vocabulary;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository {
    List<Vocabulary> findAllByUserUid(UserUid userUid);

    List<String> findAllUidsByUserUid(UserUid userUid);

    Optional<Vocabulary> findByUid(String vocabularyUid);

    Vocabulary save(Vocabulary vocabulary);

    void delete(Vocabulary vocabulary);
}
