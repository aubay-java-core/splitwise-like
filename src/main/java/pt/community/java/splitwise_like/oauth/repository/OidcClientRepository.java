package pt.community.java.splitwise_like.oauth.repository;

import org.springframework.stereotype.Repository;
import pt.community.java.splitwise_like.users.request.OidcClient;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OidcClientRepository {

    private final Map<String, OidcClient> clients = new ConcurrentHashMap<>();

    public OidcClientRepository() {
        // TODO Fazer busca via banco e implementar multclient
        OidcClient client = new OidcClient(
                "web-client",
                "secret",
                "http://localhost:8080/oidc-client.html"
        );
        clients.put(client.clientId(), client);
    }

    public Optional<OidcClient> findByClientId(String clientId) {
        return Optional.ofNullable(clients.get(clientId));
    }
}
