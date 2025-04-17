package pt.community.java.splitwise_like.oauth.service;

import pt.community.java.splitwise_like.users.request.OidcClient;

import java.util.Optional;

public interface OidcClientService {
    Optional<OidcClient> findByClientId(String clientId);
}
