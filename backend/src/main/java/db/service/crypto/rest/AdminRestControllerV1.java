package db.service.crypto.rest;


import db.service.crypto.dto.AdminDecisionDto;
import db.service.crypto.dto.P2PDto;
import db.service.crypto.dto.UserDto;
import db.service.crypto.exception.InvalidAmountException;
import db.service.crypto.exception.NoSuchP2POfferException;
import db.service.crypto.model.Admin;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.AdminService;
import db.service.crypto.service.P2PService;
import db.service.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/api/v1/admin/")
public class AdminRestControllerV1 {

    private final UserService userService;

    private final AdminService adminService;

    private final P2PService p2pService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminRestControllerV1(UserService userService, AdminService adminService, P2PService p2pService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.adminService = adminService;
        this.p2pService = p2pService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

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
    public ResponseEntity<?> makeDecision(@RequestBody AdminDecisionDto adminDecisionDto, HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Admin admin = adminService.findByUsername(username);
        if (admin == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);


        try {
            P2PDto p2pDto = adminService.makeDecision(adminDecisionDto,admin);
            return new ResponseEntity<>(p2pDto, HttpStatus.OK);
        } catch (NoSuchP2POfferException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("getAllTransactionsToCheck")
    public ResponseEntity<?> getAllTransactionsToCheck(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Admin admin = adminService.findByUsername(username);
        if (admin == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(p2pService.getAllTransactionsToCheck(),HttpStatus.OK);
    }

    @GetMapping("getStatistics")
    public ResponseEntity<?> getStatistics(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Admin admin = adminService.findByUsername(username);
        if (admin == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(p2pService.getStats(username),HttpStatus.OK);
    }







}
