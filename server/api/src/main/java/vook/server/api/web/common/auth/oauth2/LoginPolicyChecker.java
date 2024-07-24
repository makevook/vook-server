package vook.server.api.web.common.auth.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

@Component
public class LoginPolicyChecker {

    private final Boolean loginRestrictionEnable;
    private final String[] allowedEmails;

    public LoginPolicyChecker(
            @Value("${service.loginPolicy.loginRestriction.enable:false}")
            Boolean loginRestrictionEnable,
            @Value("${service.loginPolicy.loginRestriction.allowedEmails:}")
            String[] allowedEmails
    ) {
        this.loginRestrictionEnable = loginRestrictionEnable;
        this.allowedEmails = allowedEmails;
    }

    public void check(OAuth2Response response) {
        if (!loginRestrictionEnable) {
            return;
        }

        // allowedEmails가 비어 있으면 모든 이메일을 허용
        if (allowedEmails.length != 0 && !isAllowedEmail(response)) {
            throw new VookRequestRejectedException("Not allowed email: " + response.getEmail());
        }
    }

    private boolean isAllowedEmail(OAuth2Response response) {
        for (String allowedEmail : this.allowedEmails) {
            if (response.getEmail().equals(allowedEmail)) {
                return true;
            }
        }
        return false;
    }

    public static class VookRequestRejectedException extends RequestRejectedException {
        public VookRequestRejectedException(String message) {
            super(message);
        }
    }
}
