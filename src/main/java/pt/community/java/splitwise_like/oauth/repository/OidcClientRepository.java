package pt.community.java.splitwise_like.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.community.java.splitwise_like.oauth.model.OidcClient;

import java.util.Optional;

public interface OidcClientRepository extends JpaRepository<OidcClient, String> {
    Optional<OidcClient> findByClientId(String clientId);
}