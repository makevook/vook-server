package vook.server.api.app.auth;

import lombok.Getter;

@Getter
public class GeneratedToken {

    private String accessToken;
    private String refreshToken;

    public static GeneratedToken of(String accessToken, String refreshToken) {
        GeneratedToken generatedToken = new GeneratedToken();
        generatedToken.accessToken = accessToken;
        generatedToken.refreshToken = refreshToken;
        return generatedToken;
    }
}
