package pt.community.java.splitwise_like.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.community.java.splitwise_like.oauth.enuns.AuthProvider;
import pt.community.java.splitwise_like.oauth.service.TokenGeneratorService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
public class OAuth2LoginController {

    private  final TokenGeneratorService tokenService;
    private  final UserService userService;

    @GetMapping("/success")
    public ResponseEntity<?> handleOAuth2Login(OAuth2AuthenticationToken authToken) {
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Users user = userService.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProvider(AuthProvider.GOOGLE);
                    return userService.save(newUser);
                });

        String idToken = tokenService.generate(user);

        String accessToken = idToken;

        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "id_token", idToken,
                "token_type", "Bearer",
                "expires_in", 3600
        ));
    }
}
