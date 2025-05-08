package pt.community.java.splitwise_like.oauth.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthorizationCodeService {

    private final Map<String, String> codeStore = new ConcurrentHashMap<>();

    public String generateCode(String email) {
        String code = UUID.randomUUID().toString();
        codeStore.put(code, email);
        return code;
    }

    public String consumeCode(String code) {
        return codeStore.remove(code);
    }
}