package pt.community.java.splitwise_like.oauth.controller;

import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.oauth.service.TokenService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/oauth")
public class AuthController {
    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    public Map<String, String> getToken(
            @RequestParam String username,
            @RequestParam List<String> scopes,
            @RequestParam List<String> roles) {

        String token = tokenService.generateToken(username, scopes, roles);
        return Map.of("access_token", token, "token_type", "Bearer");
    }
}

