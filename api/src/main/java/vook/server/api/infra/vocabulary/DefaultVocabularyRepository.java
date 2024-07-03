package vook.server.api.infra.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.infra.vocabulary.cache.UserVocabularyCache;
import vook.server.api.infra.vocabulary.cache.UserVocabularyCacheRepository;
import vook.server.api.infra.vocabulary.jpa.VocabularyJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class DefaultVocabularyRepository implements VocabularyRepository {

    private final VocabularyJpaRepository jpaRepository;
    private final UserVocabularyCacheRepository cacheRepository;

    @Override
    public List<Vocabulary> findAllByUserUid(UserUid userUid) {
        return jpaRepository.findAllByUserUid(userUid);
    }

    @Override
    public List<String> findAllUidsByUserUid(UserUid userUid) {
        return cacheRepository.findById(userUid.getValue())
                .orElseGet(() -> {
                    List<String> vocabularyUids = jpaRepository.findAllByUserUid(userUid)
                            .stream()
                            .map(Vocabulary::getUid)
                            .toList();

                    return cacheRepository.save(new UserVocabularyCache(userUid.getValue(), vocabularyUids));
                })
                .vocabularyUids();
    }

    @Override
    public Optional<Vocabulary> findByUid(String vocabularyUid) {
        return jpaRepository.findByUid(vocabularyUid);
    }

    @Override
    public Vocabulary save(Vocabulary vocabulary) {
        Vocabulary saved = jpaRepository.save(vocabulary);

        Optional<UserVocabularyCache> cacheOptional = cacheRepository.findById(vocabulary.getUserUid().getValue());
        cacheOptional.ifPresentOrElse(
                cache -> {
                    cache.vocabularyUids().add(vocabulary.getUid());
                    cacheRepository.save(cache);
                },
                () -> cacheRepository.save(new UserVocabularyCache(vocabulary.getUserUid().getValue(), List.of(vocabulary.getUid())))
        );

        return saved;
    }

    @Override
    public void delete(Vocabulary vocabulary) {
        jpaRepository.delete(vocabulary);

        Optional<UserVocabularyCache> cache = cacheRepository.findById(vocabulary.getUserUid().getValue());
        cache.ifPresent(c -> {
            c.vocabularyUids().remove(vocabulary.getUid());
            cacheRepository.save(c);
        });
    }
}
