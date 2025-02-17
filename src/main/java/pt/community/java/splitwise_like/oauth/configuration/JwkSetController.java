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

    @GetMapping("/openid-configuration")
    public Map<String, Object> getJwks() {
        List<JWK> jwks = List.of(new RSAKey.Builder(getPublic())
                .privateKey(getPrivate())
                .algorithm(JWSAlgorithm.RS256)
                .build());
        return new JWKSet(jwks).toJSONObject();
    }

    private RSAPrivateKey getPrivate() {
        return (RSAPrivateKey) jwtConfig.keyPair.getPrivate();
    }

    private RSAPublicKey getPublic() {
        return (RSAPublicKey) jwtConfig.keyPair.getPublic();
    }
}