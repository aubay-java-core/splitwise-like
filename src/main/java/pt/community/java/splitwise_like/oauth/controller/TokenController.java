package pt.community.java.splitwise_like.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.community.java.splitwise_like.oauth.enuns.AuthProvider;
import pt.community.java.splitwise_like.oauth.service.AuthorizationCodeService;
import pt.community.java.splitwise_like.oauth.service.OidcClientService;
import pt.community.java.splitwise_like.oauth.service.TokenGeneratorService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.request.LoginRequest;
import pt.community.java.splitwise_like.users.request.OidcClient;
import pt.community.java.splitwise_like.users.request.TokenRequest;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenGeneratorService tokenGenerator;
    private final AuthorizationCodeService authorizationCodeService;
    private final OidcClientService oidcClientService;


    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody TokenRequest request) {
        if (!"authorization_code".equals(request.grant_type())) {
            return ResponseEntity.badRequest().body("invalid grant_type");
        }

        OidcClient client = oidcClientService.findByClientId(request.client_id())
                .orElse(null);

        if (client == null) {
            return ResponseEntity.status(401).body("Invalid Client");
        }
        String email = authorizationCodeService.consumeCode(request.code());

        if (email == null) {
            return ResponseEntity.status(400).body("Invalid or expired code");
        }

        Users user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String idToken = tokenGenerator.generate(user);
        String accessToken = idToken;

        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "id_token", idToken,
                "token_type", "Bearer",
                "expires_in", 3600
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Users> userOpt = userService.findByEmail(request.email());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        Users user = userOpt.get();

        if (!user.getProvider().equals(AuthProvider.LOCAL)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Use social login for this user");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String jwt = tokenGenerator.generate(user);

        return ResponseEntity.ok(Map.of("token", jwt, "email", user.getEmail(), "name", user.getName()));
    }

}

