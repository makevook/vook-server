package vook.server.api.globalcommon.helper.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import vook.server.api.globalcommon.helper.format.FormatHelper;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTWriter extends JWTHelper {

    private static final Long DEFAULT_EXPIRED_MS = 1000L * 60 * 30; // 30 minutes

    private JWSSigner signer;
    private Long expiredMs;
    private final Map<String, Object> claims = new HashMap<>();

    static JWTWriter of(String secret) {
        return run(() -> {
            JWTWriter writer = new JWTWriter();
            byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
            writer.signer = new MACSigner(secretBytes);
            return writer;
        });
    }

    public JWTWriter withExpiredMs(Long expiredMs) {
        this.expiredMs = expiredMs;
        return this;
    }

    public JWTWriter withClaim(String name, String value) {
        claims.put(name, value);
        return this;
    }

    public String jwtString() {
        return run(() -> {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + (expiredMs != null ? expiredMs : DEFAULT_EXPIRED_MS)));
            claims.forEach(builder::claim);
            JWTClaimsSet claimsSet = builder.build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            signedJWT.sign(signer);

            return signedJWT.serialize();
        }, FormatHelper.slf4j("claims: {}", claims));
    }

    public static class Builder {

        private final String secret;

        public Builder(String jwtSecret) {
            this.secret = jwtSecret;
        }

        public JWTWriter build() {
            return JWTWriter.of(secret);
        }
    }
}
