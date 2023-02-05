package db.service.crypto.security.oauth2;

import db.service.crypto.exception.UserAlreadyExistException;
import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.ClientService;
import db.service.crypto.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class AuthenticationSuccess implements AuthenticationSuccessHandler {
    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    @Autowired
    CustomOAuth2UserService oauthUserService;

    @Autowired
    UserService userService;

    @Autowired
    ClientService clientService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            String username = oauthUser.getUsername();
            User user = oauthUserService.oauthUserIntoUser(oauthUser);
            Client client = oauthUserService.oauthUserIntoClient(oauthUser);

            try {
                userService.register(user);
                clientService.createClient(client);

            } catch (UserAlreadyExistException e) {
                log.info("IN onAuthenticationSuccess - user {} has already registered", user);
            } finally {

                String token = jwtTokenProvider.createToken(username, user.getRole());

                response.sendRedirect(UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("username", user.getUsername())
                        .queryParam("token", token)
                        .queryParam("role", user.getRole())
                        .build().toUriString());

            }
        }
}
