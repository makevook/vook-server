package vook.server.api.model.user;

public enum UserStatus {
    SOCIAL_LOGIN_COMPLETED,  // 소셜로그인 완료됨
    REGISTERED,              // 가입 됨
    ONBOARDING_COMPLETED,    // 온보딩 완료 됨
    WITHDRAWN                // 탈퇴 됨
}
