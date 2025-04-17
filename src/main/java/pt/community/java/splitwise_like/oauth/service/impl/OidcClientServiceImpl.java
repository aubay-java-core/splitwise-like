package pt.community.java.splitwise_like.oauth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.oauth.repository.OidcClientRepository;
import pt.community.java.splitwise_like.oauth.service.OidcClientService;
import pt.community.java.splitwise_like.users.request.OidcClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OidcClientServiceImpl implements OidcClientService {

    private final OidcClientRepository oidcClientRepository;

    @Override
    public Optional<OidcClient> findByClientId(String clientId) {
        return oidcClientRepository.findByClientId(clientId);
    }
}
