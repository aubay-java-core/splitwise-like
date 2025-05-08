package pt.community.java.splitwise_like.oauth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.oauth.model.OidcClient;
import pt.community.java.splitwise_like.oauth.repository.OidcClientRepository;
import pt.community.java.splitwise_like.oauth.service.OidcClientService;
import pt.community.java.splitwise_like.users.request.OidcClientRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OidcClientServiceImpl implements OidcClientService {

    private final OidcClientRepository oidcClientRepository;

    @Override
    public Optional<OidcClientRequest> findByClientId(String clientId) {
        Optional<OidcClient> oidcClient = oidcClientRepository.findByClientId(clientId);
        if (oidcClient.isPresent()) {
            OidcClientRequest oidcClientRequestResponse = new OidcClientRequest(
                oidcClient.get().getClientId(),
                oidcClient.get().getClientSecret(),
                oidcClient.get().getRedirectUri()
            );
            return Optional.of(oidcClientRequestResponse);
        }
        return Optional.empty();
    }
}