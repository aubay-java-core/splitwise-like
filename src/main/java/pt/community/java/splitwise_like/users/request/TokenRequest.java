package pt.community.java.splitwise_like.users.request;

public record TokenRequest(
        String grant_type,
        String code,
        String redirect_uri,
        String client_id,
        String client_secret
) {}