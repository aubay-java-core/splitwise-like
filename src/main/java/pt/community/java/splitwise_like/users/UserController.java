package pt.community.java.splitwise_like.users;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String listUsers() {
        return "Hello from Spring boot app";
    }

    @PostMapping
    public String createUser(@RequestBody String body) {
        return "Private Route";
    }
}