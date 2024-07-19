package vook.server.api.web.common.auth.oauth2;

import java.util.Map;

public class OAuth2GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public OAuth2GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }
}
