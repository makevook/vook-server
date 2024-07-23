package vook.server.api.domain.vocabulary.model.vocabulary;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import vook.server.api.domain.vocabulary.exception.VocabularyLimitExceededException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DefaultVocabularyFactory.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class VocabularyFactoryTest {

    @MockBean
    VocabularyRepository repository;

    @Autowired
    VocabularyFactory vocabularyFactory;

    @Test
    @DisplayName("단어장 생성; 성공")
    void create() {
        // given
        when(repository.findAllByUserUid(any())).thenReturn(List.of());
        String name = "단어장 이름";
        UserUid userUid = new UserUid(UUID.randomUUID().toString());

        // when
        Vocabulary vocabulary = vocabularyFactory.create(name, userUid);

        // then
        assertThat(vocabulary.getUid()).isNotNull();
        assertThat(vocabulary.getName()).isEqualTo(name);
        assertThat(vocabulary.getUserUid()).isEqualTo(userUid);
    }

    @Test
    @DisplayName("단어장 생성; 예외 - 단어장 생성 제한 초과")
    void createFail1() {
        // given
        when(repository.findAllByUserUid(any())).thenReturn(List.of(new Vocabulary(), new Vocabulary(), new Vocabulary()));
        String name = "단어장 이름";
        UserUid userUid = new UserUid(UUID.randomUUID().toString());

        // when, then
        assertThatThrownBy(() -> vocabularyFactory.create(name, userUid))
                .isInstanceOf(VocabularyLimitExceededException.class);
    }

    @TestFactory
    @DisplayName("단어장 생성; 예외 - 단어장 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createFail2() {
        return Stream.of(
                DynamicTest.dynamicTest("단어장 이름이 null", () -> {
                    // given
                    String name = null;
                    UserUid userUid = new UserUid(UUID.randomUUID().toString());

                    // when, then
                    assertThatThrownBy(() -> vocabularyFactory.create(name, userUid))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("단어장 이름이 빈 문자열", () -> {
                    // given
                    String name = "";
                    UserUid userUid = new UserUid(UUID.randomUUID().toString());

                    // when, then
                    assertThatThrownBy(() -> vocabularyFactory.create(name, userUid))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("사용자 UID가 null", () -> {
                    // given
                    String name = "단어장 이름";
                    UserUid userUid = null;

                    // when, then
                    assertThatThrownBy(() -> vocabularyFactory.create(name, userUid))
                            .isInstanceOf(ConstraintViolationException.class);
                })
        );
    }
}
