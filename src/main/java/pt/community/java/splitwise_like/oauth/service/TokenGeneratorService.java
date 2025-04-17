package pt.community.java.splitwise_like.oauth.service;

import pt.community.java.splitwise_like.users.model.Users;

public interface TokenGeneratorService {
    String generate(Users user);
}
