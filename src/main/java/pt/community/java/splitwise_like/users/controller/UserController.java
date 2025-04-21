package pt.community.java.splitwise_like.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.oauth.enuns.AuthProvider;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.request.UserRegisterRequest;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('USER') and hasAuthority('SCOPE_users:read-financial')")
    public String toString() {
        return "Private Route";
    }



    @GetMapping("/email/{email}")
    @PostAuthorize("returnObject.email == authentication.name or hasRole('ADMIN')")
    public Users findByEmail(@PathVariable String email) {
        return userService.findByEmail(email).orElse(null);
    }


    @GetMapping("/debug")
    public List<String> getRoles(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("email");

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            List<String> result = new ArrayList<>();
            result.add("Email: " + email);
            result.addAll(roles);

            return result;
        }

        return List.of("Email Not found");
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        if (userService.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        Users user = new Users();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setProvider(AuthProvider.LOCAL);

        userService.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

}