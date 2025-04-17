package pt.community.java.splitwise_like.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.oauth.enuns.AuthProvider;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.request.UserRegisterRequest;
import pt.community.java.splitwise_like.users.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("ROLE_ADMIN")
    public String toString() {
        return "Private Route";
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