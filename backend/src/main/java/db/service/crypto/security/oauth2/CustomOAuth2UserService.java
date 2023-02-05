package db.service.crypto.security.oauth2;

import db.service.crypto.model.Client;
import db.service.crypto.model.Role;
import db.service.crypto.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }

    public User oauthUserIntoUser(CustomOAuth2User oAuth2User){
        User user = new User();
        user.setPassword("google_password");
        user.setRole(Role.ROLE_CLIENT);
        user.setUsername(oAuth2User.getUsername());
        return user;
    }

    public Client oauthUserIntoClient(CustomOAuth2User customOAuth2User){
        Client client = new Client();
        client.setUserLogin(customOAuth2User.getUsername());
        client.setName(customOAuth2User.getName());
        client.setSurname(customOAuth2User.getSurname());
        return client;
    }

}