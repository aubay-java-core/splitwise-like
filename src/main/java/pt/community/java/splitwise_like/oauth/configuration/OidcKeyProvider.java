package pt.community.java.splitwise_like.oauth.configuration;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.oauth.utils.PemUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
public class OidcKeyProvider {

    private final RSAKey rsaJwk;

    public OidcKeyProvider() {
        try {

            RSAPrivateKey privateKey = PemUtils.readPrivateKey("/keys/private_key.pem");
            RSAPublicKey publicKey = PemUtils.readPublicKey("/keys/public_key.pem");

            this.rsaJwk = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID("splitwise-key")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chaves RSA", e);
        }
    }

    public RSAKey getRsaJwk() {
        return rsaJwk;
    }

    public RSAKey getPublicJwk() {
        return rsaJwk.toPublicJWK();
    }
}