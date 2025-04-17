package pt.community.java.splitwise_like.oauth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.community.java.splitwise_like.oauth.configuration.OidcKeyProvider;

import java.util.Map;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class OpenIdConfigurationController {

    private final OidcKeyProvider keyProvider;

    @GetMapping("/jwks.json")
    public Map<String, Object> keys() {
        RSAKey rsaKey = keyProvider.getPublicJwk();
        return new JWKSet(rsaKey).toJSONObject();
    }
    @GetMapping("/openid-configuration")
    public Map<String, Object> openIdConfiguration() {
        return Map.of(
                "issuer", "http://localhost:8080",
                "authorization_endpoint", "http://localhost:8080/authorize",
                "token_endpoint", "http://localhost:8080/token",
                "userinfo_endpoint", "http://localhost:8080/userinfo", // TODO deixar como desafio implementar
                "jwks_uri", "http://localhost:8080/.well-known/jwks.json",
                "response_types_supported", new String[]{"code"},
                "subject_types_supported", new String[]{"public"},
                "id_token_signing_alg_values_supported", new String[]{"RS256"},
                "scopes_supported", new String[]{"openid", "profile", "email"},
                "grant_types_supported", new String[]{"authorization_code"}
        );
    }
}