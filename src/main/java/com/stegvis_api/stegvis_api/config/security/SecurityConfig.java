package com.stegvis_api.stegvis_api.config.security;

// import com.stegvis_api.stegvis_api.auth.service.AuthService;
// import com.stegvis_api.stegvis_api.auth.service.OAuthService;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtTokenFilter jwtTokenFilter;
        // private final OAuthService oAuthService;
        // private final AuthService authService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/auth/**"
                                                // "/api/oauth2/**",
                                                // "/login/oauth2/**"
                                                ).permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
                                "http://localhost:5173",
                                "http://localhost:8080",
                                "https://clownfish-app-qahqf.ondigitalocean.app"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }

        /*
         * @Bean
         * public AuthenticationSuccessHandler oAuth2SuccessHandler() {
         * return (request, response, authentication) -> {
         * OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)
         * authentication;
         * var attributes = oauthToken.getPrincipal().getAttributes();
         *
         * String provider = "google";
         * String oauthId = (String) attributes.get("sub");
         * String email = (String) attributes.get("email");
         * String firstName = (String) attributes.get("given_name");
         * String lastName = (String) attributes.get("family_name");
         * Set<String> scopes = Set.of("openid", "profile", "email");
         *
         * User user = oAuthService.findByEmail(email)
         * .map(existingUser -> oAuthService.linkOAuthToUser(existingUser, provider,
         * oauthId, scopes))
         * .orElseGet(() -> oAuthService.createNewOAuthUser(email, provider, oauthId,
         * scopes, firstName, lastName));
         *
         * authService.setTokens(user, response);
         * response.sendRedirect("http://localhost:5173/oauth2/success");
         * };
         * }
         */
}
