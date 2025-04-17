package pt.community.java.splitwise_like.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import pt.community.java.splitwise_like.oauth.service.AuthorizationCodeService;

import java.io.IOException;

@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationCodeService authorizationCodeService;

    @GetMapping
    public void authorize(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException {

        if (user == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String clientId = request.getParameter("client_id");
        String redirectUri = request.getParameter("redirect_uri");
        String state = request.getParameter("state");


        String code = authorizationCodeService.generateCode(user.getUsername());

        String redirect = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("client_id", clientId)
                .queryParam("code", code)
                .queryParam("state", state)
                .build().toUriString();

        response.sendRedirect(redirect);
    }
}
