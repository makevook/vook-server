package vook.server.poc.springdataredis.usecases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vook.server.poc.springdataredis.model.User;
import vook.server.poc.springdataredis.testhelper.IntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UseRedisRepositoryTest extends IntegrationTestBase {

    @Autowired
    UseRedisRepository useRedisRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 생성 - 성공")
    void createUser() {
        // given
        String id = "1";
        List<String> accessVocabularies = List.of("a", "b");

        // when
        User user = useRedisRepository.createUser(id, accessVocabularies);

        // then
        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.allowedVocabularies()).containsExactlyInAnyOrderElementsOf(accessVocabularies);
    }

    @Test
    @DisplayName("유저 조회 - 성공")
    void getUser() {
        // given
        String id = "1";
        List<String> accessVocabularies = List.of("a", "b");
        useRedisRepository.createUser(id, accessVocabularies);

        // when
        User user = useRedisRepository.getUser(id);

        // then
        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.allowedVocabularies()).containsExactlyInAnyOrderElementsOf(accessVocabularies);
    }

    @Test
    @DisplayName("유저 삭제 - 성공")
    void deleteUser() {
        // given
        String id = "1";
        List<String> accessVocabularies = List.of("a", "b");
        useRedisRepository.createUser(id, accessVocabularies);

        // when
        useRedisRepository.deleteUser(id);

        // then
        User user = useRedisRepository.getUser(id);
        assertThat(user).isNull();
    }

}
