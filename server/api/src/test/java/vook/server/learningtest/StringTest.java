package vook.server.learningtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTest {

    @Test
    @DisplayName("문자열에 startWith를 빈 문자열을 넣을 경우 무조건 true를 반환한다.")
    void startWithEmptyString() {
        // given
        String str = "hello";

        // when
        boolean result = str.startsWith("");
        
        // then
        assertThat(result).isTrue();
    }
}
