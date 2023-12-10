package db.service.crypto.rest;

import db.service.crypto.dto.AuthenticationRequestDto;
import db.service.crypto.dto.RegistrationRequestDto;
import db.service.crypto.exception.InvalidRequestException;
import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.ClientService;
import db.service.crypto.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestControllerV1 {

    private final UserService userService;
    private final ClientService clientService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        log.info("POST --> /api/v1/auth/login");
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
        log.info("POST --> /api/v1/auth/register");
        this.validateRequest(requestDto);
        User userToAdd = User.builder()
                .username(requestDto.getUsername().trim())
                .password(requestDto.getPassword().trim())
                .build();
        Client clientToAdd = Client.builder()
                .userLogin(requestDto.getUsername().trim())
                .name(requestDto.getName().trim())
                .surname(requestDto.getSurname().trim())
                .build();
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
