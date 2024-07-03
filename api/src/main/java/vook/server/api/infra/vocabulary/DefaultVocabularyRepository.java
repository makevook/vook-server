package vook.server.api.infra.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DefaultVocabularyRepository implements VocabularyRepository {

    private final VocabularyJpaRepository jpaRepository;

    @Override
    public List<Vocabulary> findAllByUserUid(UserUid userUid) {
        return jpaRepository.findAllByUserUid(userUid);
    }

    @Override
    public Optional<Vocabulary> findByUid(String vocabularyUid) {
        return jpaRepository.findByUid(vocabularyUid);
    }

    @Override
    public Vocabulary save(Vocabulary vocabulary) {
        return jpaRepository.save(vocabulary);
    }

    @Override
    public void delete(Vocabulary vocabulary) {
        jpaRepository.delete(vocabulary);
    }
}
