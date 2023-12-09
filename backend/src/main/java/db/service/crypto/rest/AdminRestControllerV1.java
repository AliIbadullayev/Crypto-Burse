package db.service.crypto.rest;


import db.service.crypto.dto.AdminDecisionDto;
import db.service.crypto.dto.P2PDto;
import db.service.crypto.dto.UserDto;
import db.service.crypto.exception.JwtTokenIsEmptyException;
import db.service.crypto.exception.UserNotFoundException;
import db.service.crypto.model.Admin;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.AdminService;
import db.service.crypto.service.P2PService;
import db.service.crypto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin/")
public class AdminRestControllerV1 {

    private final P2PService p2pService;
    private final UserService userService;
    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "users/{username}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("makeDecision")
    public ResponseEntity<?> makeDecision(@RequestBody AdminDecisionDto adminDecisionDto, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Admin admin = adminService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        P2PDto p2pDto = adminService.makeDecision(adminDecisionDto, admin);
        return new ResponseEntity<>(p2pDto, HttpStatus.OK);
    }

    @GetMapping("getAllTransactionsToCheck")
    public ResponseEntity<?> getAllTransactionsToCheck(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        adminService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(p2pService.getAllTransactionsToCheck(), HttpStatus.OK);
    }

    @GetMapping("getStatistics")
    public ResponseEntity<?> getStatistics(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        adminService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(p2pService.getStats(username), HttpStatus.OK);
    }
}
