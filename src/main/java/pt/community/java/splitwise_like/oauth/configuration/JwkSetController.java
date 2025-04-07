package pt.community.java.splitwise_like.oauth.configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/.well-known")
public class JwkSetController {

    private final JwtConfig jwtConfig;

    public JwkSetController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @GetMapping("/jwks.json")
    public Map<String, Object> getJwks() {
        RSAPublicKey publicKey = jwtConfig.getPublicKey();

        JWK jwk = new RSAKey.Builder(publicKey)
                .keyID("auth-key-2025") // TODO Gerar dinamicamente
                .algorithm(JWSAlgorithm.RS256)
                .build();

        return new JWKSet(List.of(jwk)).toJSONObject();
    }

    @GetMapping("/openid-configuration")
    public Map<String, Object> getOpenIdConfiguration() {
        return Map.of(
                "issuer", "http://localhost:8080",
                "jwks_uri", "http://localhost:8080/.well-known/jwks.json",
                "id_token_signing_alg_values_supported", List.of("RS256")
        );
    }
}