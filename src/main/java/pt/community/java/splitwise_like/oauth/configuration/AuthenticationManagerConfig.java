package pt.community.java.splitwise_like.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import pt.community.java.splitwise_like.oauth.service.CustomAuthenticationProvider;

@Configuration
public class AuthenticationManagerConfig {

    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customProvider) {
        return new ProviderManager(customProvider);
    }
}
