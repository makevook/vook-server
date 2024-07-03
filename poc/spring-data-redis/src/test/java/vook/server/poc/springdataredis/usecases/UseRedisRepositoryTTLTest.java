package vook.server.poc.springdataredis.usecases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import vook.server.poc.springdataredis.testhelper.IntegrationTestBase;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

class UseRedisRepositoryTTLTest extends IntegrationTestBase {

    @Autowired
    UseRedisRepository useRedisRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("TTL 적용 확인")
    void ttl() throws InterruptedException {
        // given
        String id = "1";

        // when
        useRedisRepository.createUser(id, List.of("a", "b"));

        // then
        assertThat(userRepository.findById(id)).isNotEmpty();

        sleep(1_000);

        assertThat(userRepository.findById(id)).isEmpty();
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public TimeToLiveSupplier timeToLiveSupplier() {
            return () -> 1L;
        }
    }
}
