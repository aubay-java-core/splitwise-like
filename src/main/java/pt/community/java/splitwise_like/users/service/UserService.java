package pt.community.java.splitwise_like.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.Optional;

public interface UserService {
    Optional<Users> findByEmail(String email);

    Users save(Users user);

    UserDetails loadUserByUsername(String email);
}
