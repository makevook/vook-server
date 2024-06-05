package vook.server.api.testhelper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class HttpEntityBuilder {

    private final HttpHeaders headers = new HttpHeaders();

    public HttpEntityBuilder addHeader(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public HttpEntity<?> build() {
        return new HttpEntity<>(headers);
    }
}
