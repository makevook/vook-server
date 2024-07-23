package vook.server.api.web.common.auth.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.user.logic.UserSignUpFromSocialCommand;
import vook.server.api.domain.user.model.social_user.SocialUser;
import vook.server.api.web.common.auth.data.VookLoginUser;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VookOAuth2UserService extends DefaultOAuth2UserService {

    private final UserLogic userLogic;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.debug("OAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = toOAuth2Response(registrationId, oAuth2User);
        if (oAuth2Response == null) {
            log.warn("Unsupported registrationId: {}", registrationId);
            return null;
        }

        return userLogic.findByProvider(oAuth2Response.getProvider(), oAuth2Response.getProviderId())
                .map(VookLoginUser::from)
                .orElseGet(() -> signUpUser(oAuth2Response));
    }

    private static OAuth2Response toOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        return switch (registrationId) {
            case "google" -> new OAuth2GoogleResponse(oAuth2User.getAttributes());
            default -> null;
        };
    }

    private VookLoginUser signUpUser(OAuth2Response oAuth2Response) {
        UserSignUpFromSocialCommand command = UserSignUpFromSocialCommand.builder()
                .provider(oAuth2Response.getProvider())
                .providerUserId(oAuth2Response.getProviderId())
                .email(oAuth2Response.getEmail())
                .build();
        SocialUser saved = userLogic.signUpFromSocial(command);
        return VookLoginUser.from(saved);
    }
}
