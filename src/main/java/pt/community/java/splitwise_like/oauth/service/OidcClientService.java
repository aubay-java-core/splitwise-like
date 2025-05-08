package pt.community.java.splitwise_like.oauth.service;

import pt.community.java.splitwise_like.users.request.OidcClientRequest;

import java.util.Optional;

public interface OidcClientService {
    Optional<OidcClientRequest> findByClientId(String clientId);
}
