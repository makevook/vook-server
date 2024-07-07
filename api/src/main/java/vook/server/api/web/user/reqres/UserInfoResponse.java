package vook.server.api.web.user.reqres;

import lombok.Builder;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.model.UserStatus;

@Builder
public record UserInfoResponse(
        String uid,
        String email,
        UserStatus status,
        Boolean onboardingCompleted,
        String nickname
) {
    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .uid(user.getUid())
                .email(user.getEmail())
                .status(user.getStatus())
                .onboardingCompleted(user.getOnboardingCompleted())
                .nickname(user.getUserInfo() != null ? user.getUserInfo().getNickname() : null)
                .build();
    }
}
