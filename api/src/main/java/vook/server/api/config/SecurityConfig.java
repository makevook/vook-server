package vook.server.api.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import vook.server.api.web.auth.app.TokenService;
import vook.server.api.web.auth.jwt.JWTFilter;
import vook.server.api.web.auth.oauth2.LoginSuccessHandler;
import vook.server.api.web.auth.oauth2.VookOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${service.oauth2.loginFailUrl}")
    private String loginFailUrl;

    private final VookOAuth2UserService oAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final TokenService tokenService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        disableDefaultSecurity(http);

        http.authorizeHttpRequests(c -> c
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
        );

        http.oauth2Login(c -> c
                .userInfoEndpoint(ec -> ec
                        .userService(oAuth2UserService)
                )
                .authorizationEndpoint(ec -> ec
                        .authorizationRequestResolver(authorizationRequestResolver(clientRegistrationRepository))
                )
                .successHandler(loginSuccessHandler)
                .failureHandler((request, response, exception) -> response.sendRedirect(loginFailUrl))
        );

        http.addFilterAfter(new JWTFilter(tokenService), OAuth2LoginAuthenticationFilter.class);

        http.exceptionHandling(e -> e
                // 인증되지 않은 사용자의 요청 처리
                .authenticationEntryPoint((request, response, authException) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                // 인가되지 않은 사용자의 요청 처리
                .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
        );

        return http.build();
    }

    private static void disableDefaultSecurity(HttpSecurity http) throws Exception {
        //csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        //HTTP Basic 인증 방식 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        //Spring Security 세션 사용 비활성화
        http.sessionManagement(c -> c
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    }

    /**
     * OAuth2 인증 요청시 consent 확인을 강제하는 옵션 추가를 위해 사용
     */
    private static OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
        );
        authorizationRequestResolver.setAuthorizationRequestCustomizer(c -> c
                .additionalParameters(params -> {
                    params.put("prompt", "consent");
                }));
        return authorizationRequestResolver;
    }
}
