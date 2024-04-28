package vook.server.api.web.routes.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vook.server.api.testhelper.ApiTest;

import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest extends ApiTest {

    @Test
    @DisplayName("헬스체크")
    void health() {
        // when
        ResponseEntity<String> res = rest.getForEntity("/health", String.class);

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo("OK");
    }
}
