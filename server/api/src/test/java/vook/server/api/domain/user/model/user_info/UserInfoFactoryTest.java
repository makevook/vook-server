package vook.server.api.domain.user.model.user_info;

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

@SpringBootTest(classes = DefaultUserInfoFactory.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class UserInfoFactoryTest {

    @Autowired
    UserInfoFactory factory;

    @Test
    @DisplayName("유저 정보 생성; 정상")
    void createForRegisterOf() {
        // given
        User user = mock(User.class);

        // when
        UserInfo userInfo = factory.createForRegisterOf("nickname", true, user);

        // then
        assertThat(userInfo.getNickname()).isEqualTo("nickname");
        assertThat(userInfo.getMarketingEmailOptIn()).isTrue();
        assertThat(userInfo.getUser()).isEqualTo(user);

        verify(user, times(1)).validateRegisterProcessReady();
    }

    @TestFactory
    @DisplayName("유저 정보 생성; 예외 - 유저 정보 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createForRegisterOfFail2() {
        return Stream.of(
                DynamicTest.dynamicTest("nickname이 null", () -> {
                    assertThatThrownBy(() -> factory.createForRegisterOf(null, true, mock(User.class)))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("nickname이 빈 문자열", () -> {
                    assertThatThrownBy(() -> factory.createForRegisterOf("", true, mock(User.class)))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("marketingEmailOptIn이 null", () -> {
                    assertThatThrownBy(() -> factory.createForRegisterOf("nickname", null, mock(User.class)))
                            .isInstanceOf(ConstraintViolationException.class);
                }),
                DynamicTest.dynamicTest("user가 null", () -> {
                    assertThatThrownBy(() -> factory.createForRegisterOf("nickname", true, null))
                            .isInstanceOf(ConstraintViolationException.class);
                })
        );
    }
}
