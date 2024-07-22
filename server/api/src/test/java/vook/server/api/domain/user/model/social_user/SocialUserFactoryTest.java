package vook.server.api.domain.user.model.social_user;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import vook.server.api.domain.user.model.user.User;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = DefaultSocialUserFactory.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class SocialUserFactoryTest {

    @Autowired
    SocialUserFactory factory;

    @Test
    @DisplayName("소셜 유저 생성; 정상")
    void createForNewOf() {
        // given
        User user = mock(User.class);

        // when
        SocialUser socialUser = factory.createForNewOf("provider", "providerUserId", user);

        // then
        assertThat(socialUser.getProvider()).isEqualTo("provider");
        assertThat(socialUser.getProviderUserId()).isEqualTo("providerUserId");
        assertThat(socialUser.getUser()).isEqualTo(user);

        verify(user, times(1)).addSocialUser(socialUser);
    }

    @TestFactory
    @DisplayName("소셜 유저 생성; 예외 - 소셜 유서 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createForNewOfFail() {
        return Stream.of(
                DynamicTest.dynamicTest("provider가 null", () -> {
                    assertThatThrownBy(() -> factory.createForNewOf(null, "providerUserId", new User()))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("provider가 빈 문자열", () -> {
                    assertThatThrownBy(() -> factory.createForNewOf("", "providerUserId", new User()))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("providerUserId가 null", () -> {
                    assertThatThrownBy(() -> factory.createForNewOf("provider", null, new User()))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("providerUserId가 빈 문자열", () -> {
                    assertThatThrownBy(() -> factory.createForNewOf("provider", "", new User()))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("user가 null", () -> {
                    assertThatThrownBy(() -> factory.createForNewOf("provider", "providerUserId", null))
                            .isInstanceOf(ConstraintViolationException.class);
                })
        );
    }
}
