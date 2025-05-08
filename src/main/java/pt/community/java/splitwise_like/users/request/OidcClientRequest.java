package pt.community.java.splitwise_like.users.request;

public record OidcClientRequest(
        String clientId,
        String clientSecret,
        String redirectUri
) {}