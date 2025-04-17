package pt.community.java.splitwise_like.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
