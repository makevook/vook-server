package vook.server.api.domain.user.model.user;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = DefaultUserFactory.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class UserFactoryTest {

    @Autowired
    UserFactory factory;

    @Test
    @DisplayName("유저 생성; 정상")
    void createForSignUpFromSocialOf() {
        // when
        User user = factory.createForSignUpFromSocialOf("abc@example.com");

        // then
        assertThat(user.getUid()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("abc@example.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.SOCIAL_LOGIN_COMPLETED);
        assertThat(user.getOnboardingCompleted()).isFalse();
        assertThat(user.getSocialUsers()).isEmpty();
    }

    @TestFactory
    @DisplayName("유저 생성; 예외 - 유저 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createForSignUpFromSocialOfFail() {
        return Stream.of(
                DynamicTest.dynamicTest("email이 null", () -> {
                    assertThatThrownBy(() -> factory.createForSignUpFromSocialOf(null))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("email이 빈 문자열", () -> {
                    assertThatThrownBy(() -> factory.createForSignUpFromSocialOf(""))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("email이 이메일 형식이 아님", () -> {
                    assertThatThrownBy(() -> factory.createForSignUpFromSocialOf("abc"))
                            .isInstanceOf(ConstraintViolationException.class);
                })
        );
    }
}
