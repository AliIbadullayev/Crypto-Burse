package db.service.crypto.security.oauth2;

import db.service.crypto.model.Client;
import db.service.crypto.model.Role;
import db.service.crypto.model.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }

    public User oauthUserIntoUser(CustomOAuth2User oAuth2User) {
        return User.builder()
                .password("google_password")
                .role(Role.ROLE_CLIENT)
                .username(oAuth2User.getUsername())
                .build();
    }

    public Client oauthUserIntoClient(CustomOAuth2User customOAuth2User) {
        return Client.builder()
                .userLogin(customOAuth2User.getUsername())
                .name(customOAuth2User.getName())
                .surname(customOAuth2User.getSurname())
                .build();
    }
}