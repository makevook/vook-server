package vook.server.poc.springdataredis.usecases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import vook.server.poc.springdataredis.model.User;
import vook.server.poc.springdataredis.testhelper.IntegrationTestBase;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UseRedisTemplateTest extends IntegrationTestBase {

    @Autowired
    UseRedisTemplate useRedisTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    @Test
    @DisplayName("유저 생성 - 성공")
    void createUser() {
        // given
        String id = "1";
        List<String> accessVocabularies = List.of("a", "b");

        // when
        User user = useRedisTemplate.createUser(id, accessVocabularies);

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
        useRedisTemplate.createUser(id, accessVocabularies);

        // when
        User user = useRedisTemplate.getKEY(id);

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
        useRedisTemplate.createUser(id, accessVocabularies);

        // when
        useRedisTemplate.deleteUser(id);

        // then
        assertThat(useRedisTemplate.getKEY(id)).isNull();
    }

    @Test
    @DisplayName("디버깅을 위한 테스트")
    void valueCheck() {
        useRedisTemplate.createUser("1", List.of("a", "b"));

        // redis에 존재하는 모든 key를 얻는다.
        Set<String> keys = redisTemplate.keys("*");
        assertThat(keys).hasSize(1);
        keys.forEach(k -> {
            System.out.println("key: " + k);
            System.out.println("value: " + redisTemplate.opsForHash().values(k));
        });
    }
}
