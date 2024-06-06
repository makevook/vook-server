package vook.server.api.testhelper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class HttpEntityBuilder {

    private Object body;
    private final HttpHeaders headers = new HttpHeaders();

    public HttpEntityBuilder header(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public HttpEntityBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public HttpEntity<?> build() {
        return new HttpEntity<>(body, headers);
    }
}
