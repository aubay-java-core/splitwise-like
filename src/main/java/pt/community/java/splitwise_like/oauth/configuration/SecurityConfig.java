package pt.community.java.splitwise_like.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.ArrayList;

import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.authenticated;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(
                                "/auth/**",
                                "/oauth/**",
                                "/oauth2/**",
                                "/login/**",
                                "/.well-known/**",
                                "/token",
                                "/userinfo",
                                "/oidc-client.html",
                                "/static/**"

                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").access(hasScope("users:read"))
                        .requestMatchers(HttpMethod.POST, "/users/**").access(hasScope("users:write"))
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/auth/oauth2/success", true)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthorityPrefix("SCOPE_");
        scopeConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = new ArrayList<GrantedAuthority>();

            authorities.addAll(scopeConverter.convert(jwt));

            var roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }

            return authorities;
        });

        converter.setPrincipalClaimName("email");

        return converter;
    }

    private AuthorizationManager<RequestAuthorizationContext> hasScope(String scope) {
        return AuthorizationManagers.allOf(
                authenticated(),
                hasAuthority("SCOPE_" + scope)
        );
    }
}