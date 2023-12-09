package db.service.crypto.rest;

import db.service.crypto.dto.AuthenticationRequestDto;
import db.service.crypto.dto.RegistrationRequestDto;
import db.service.crypto.exception.InvalidRequestException;
import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.ClientService;
import db.service.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager,
                                          JwtTokenProvider jwtTokenProvider, UserService userService,
                                          ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientService = clientService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        String username = requestDto.getUsername().trim();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        String token = jwtTokenProvider.createToken(username, user.getRole());

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDto requestDto) {
        User userToAdd = new User();
        Client clientToAdd = new Client();
        this.validateRequest(requestDto);
        userToAdd.setUsername(requestDto.getUsername().trim());
        userToAdd.setPassword(requestDto.getPassword().trim());
        clientToAdd.setUserLogin(requestDto.getUsername().trim());
        clientToAdd.setName(requestDto.getName().trim());
        clientToAdd.setSurname(requestDto.getSurname().trim());
        userService.register(userToAdd);
        clientService.createClient(clientToAdd);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    private void validateRequest(RegistrationRequestDto requestDto) {
        if (requestDto.getName() == null ||
                requestDto.getSurname() == null ||
                requestDto.getPassword() == null ||
                requestDto.getUsername() == null) {
            throw new InvalidRequestException("Null поля в запросе!");
        }
        if (requestDto.getUsername().trim().contains(" ") ||
                requestDto.getPassword().trim().contains(" ")) {
            throw new InvalidRequestException("Логин и пароль не могут содержать пробелы!");
        }
    }
}
