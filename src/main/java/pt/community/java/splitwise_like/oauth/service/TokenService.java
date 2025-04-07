package pt.community.java.splitwise_like.oauth.service;

import java.util.List;

public interface TokenService {

    String generateToken(String username, List<String> scopes, List<String> roles);
}