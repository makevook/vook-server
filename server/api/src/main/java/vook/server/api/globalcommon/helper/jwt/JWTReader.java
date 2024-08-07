package vook.server.api.globalcommon.helper.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import vook.server.api.globalcommon.helper.format.FormatHelper;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTReader extends JWTHelper {

    private JWSVerifier verifier;
    private SignedJWT signedJWT;

    static JWTReader of(String secret, String token) {
        return run(() -> {
            JWTReader reader = new JWTReader();
            byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
            reader.verifier = new MACVerifier(secretBytes);
            reader.signedJWT = SignedJWT.parse(token);
            return reader;
        }, FormatHelper.slf4j("token: {}", token));
    }

    public void validate() {
        run(() -> {
            if (!signedJWT.verify(verifier)) {
                throw new JWTException(new IllegalArgumentException("JWT의 서명이 올바르지 않습니다."));
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime.before(new Date())) {
                throw new JWTException(new IllegalArgumentException("JWT가 만료되었습니다."));
            }

            return null;
        }, FormatHelper.slf4j("signedJWT: {}, verifier: {}", signedJWT.serialize(), verifier));
    }

    public String getClaim(String claimName) {
        return run(
                () -> signedJWT.getJWTClaimsSet().getStringClaim(claimName),
                FormatHelper.slf4j("claimName: {}, signedJWT: {}", claimName, signedJWT.serialize())
        );
    }

    public static class Builder {

        private final String secret;

        public Builder(String jwtSecret) {
            this.secret = jwtSecret;
        }

        public JWTReader build(String token) {
            return JWTReader.of(secret, token);
        }
    }
}
