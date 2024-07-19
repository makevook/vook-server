package vook.server.api.infra.vocabulary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.infra.vocabulary.cache.UserVocabularyCache;
import vook.server.api.infra.vocabulary.cache.UserVocabularyCacheRepository;
import vook.server.api.testhelper.IntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class DefaultVocabularyRepositoryTest extends IntegrationTestBase {

    @Autowired
    DefaultVocabularyRepository defaultVocabularyRepository;

    @Autowired
    UserVocabularyCacheRepository userVocabularyCacheRepository;

    @AfterEach
    void tearDown() {
        userVocabularyCacheRepository.deleteAll();
    }

    @Test
    @DisplayName("용어집 생성 - 정상")
    void save() {
        // given
        String name = "용어집1";
        UserUid userUid = new UserUid("user-uid");
        Vocabulary vocabulary = Vocabulary.forCreateOf(name, userUid);

        // when
        Vocabulary saved = defaultVocabularyRepository.save(vocabulary);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUid()).isNotNull();
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getUserUid()).isEqualTo(userUid);

        UserVocabularyCache cache = userVocabularyCacheRepository.findById(userUid.getValue()).orElseThrow();
        assertThat(cache.vocabularyUids()).containsExactly(saved.getUid());
    }

    @Test
    @DisplayName("용어집 삭제 - 정상")
    void delete() {
        // given
        String name = "용어집1";
        UserUid userUid = new UserUid("user-uid");
        Vocabulary saved = defaultVocabularyRepository.save(Vocabulary.forCreateOf(name, userUid));

        UserVocabularyCache savedCache = userVocabularyCacheRepository.findById(userUid.getValue()).orElseThrow();
        assertThat(savedCache.vocabularyUids()).containsExactly(saved.getUid());

        // when
        defaultVocabularyRepository.delete(saved);

        // then
        assertThat(defaultVocabularyRepository.findByUid(saved.getUid())).isEmpty();

        UserVocabularyCache deletedCache = userVocabularyCacheRepository.findById(userUid.getValue()).orElseThrow();
        assertThat(deletedCache.vocabularyUids()).isEmpty();
    }

    @Test
    @DisplayName("유저 ID에 따른 접근 가능한 용어집 목록 조회 - 정상")
    void findAllByUserUid() {
        // given
        UserUid userUid = new UserUid("user-uid");
        Vocabulary vocabulary1 = defaultVocabularyRepository.save(Vocabulary.forCreateOf("용어집1", userUid));
        Vocabulary vocabulary2 = defaultVocabularyRepository.save(Vocabulary.forCreateOf("용어집2", userUid));

        // when
        List<String> vocabularyUids = defaultVocabularyRepository.findAllUidsByUserUid(userUid);

        // then
        assertThat(vocabularyUids).containsExactlyInAnyOrder(vocabulary1.getUid(), vocabulary2.getUid());
    }
}
