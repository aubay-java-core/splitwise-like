package pt.community.java.splitwise_like.users.request;

public record OidcClient(
        String clientId,
        String clientSecret,
        String redirectUri
) {}