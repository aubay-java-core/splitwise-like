package pt.community.java.splitwise_like.oauth.service.impl;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.oauth.configuration.OidcKeyProvider;
import pt.community.java.splitwise_like.oauth.service.TokenGeneratorService;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

    private final OidcKeyProvider keyProvider;

    public String generate(Users user) {
        try {
            JWSSigner signer = new RSASSASigner(keyProvider.getRsaJwk().toPrivateKey());


            List<String> scopes = new ArrayList<>(List.of("users:read", "users:write"));
            List<String> roles = new ArrayList<>(List.of("USER")); // TODO Desafio: Refatorar para buscar no base de dados.

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer("http://localhost:8080")
                    .subject(user.getId().toString())
                    .audience("web-client")
                    .claim("email", user.getEmail())
                    .claim("name", user.getName())
                    .claim("scope", String.join(" ", scopes))
                    .claim("roles", roles)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyProvider.getRsaJwk().getKeyID()).build(),
                    claims
            );

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar ID Token", e);
        }
    }
}
