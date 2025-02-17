package pt.community.java.splitwise_like.oauth.controller;

import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@CrossOrigin("*")
@RequestMapping("/oauth")
public class AuthController {
    private final JwtEncoder jwtEncoder;

    public AuthController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenResponse> generateToken(@RequestParam("client_id") String clientId, @RequestParam("client_secret") String secret) {
        if ("123".equals(clientId) && "123".equals(secret)) {
            return ResponseEntity.ok(createAndSignToken("users:read"));
        }
        if ("456".equals(clientId) && "123".equals(secret)) {
            return ResponseEntity.ok(createAndSignToken("users:write"));
        }
        return ResponseEntity.status(401).body(new TokenError());
    }

    private TokenDTO createAndSignToken(String scope) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("local")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject("api")
                .claim("scope", scope)
                .build();
        return new TokenDTO(scope, jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
    }
}

interface TokenResponse {
}

@Getter
class TokenDTO implements TokenResponse {
    private final String token_type = "client_credentials";
    private final int expires_in = 3600;
    private final String access_token;
    private final String scope;

    public TokenDTO(String scope, String accessToken) {
        this.scope = scope;
        this.access_token = accessToken;
    }
}

@Getter
class TokenError implements TokenResponse {
    private final String timestamp = LocalDateTime.now().toString();
    private final String status = "401";
    private final String error = "Bad Request";
    private final String message = "Invalid credentials";
    private final String path = "/oauth/token";
}
