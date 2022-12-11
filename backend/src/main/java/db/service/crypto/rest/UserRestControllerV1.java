package db.service.crypto.rest;

import db.service.crypto.dto.AddCardRequestDto;
import db.service.crypto.dto.FiatDepositDto;
import db.service.crypto.dto.TransactionRequestDto;
import db.service.crypto.dto.UserDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.BankCard;
import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.BankCardService;
import db.service.crypto.service.ClientService;
import db.service.crypto.service.TransactionService;
import db.service.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/api/v1/users/")
public class UserRestControllerV1 {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final ClientService clientService;

    private final BankCardService bankCardService;

    private final TransactionService transactionService;

    @Autowired
    public UserRestControllerV1(JwtTokenProvider jwtTokenProvider, UserService userService, ClientService clientService, BankCardService bankCardService, TransactionService transactionService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientService = clientService;
        this.bankCardService = bankCardService;
        this.transactionService = transactionService;
    }

    @GetMapping(value = "{username}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping(value = "addCard")
    public ResponseEntity<String> addCard(@RequestBody AddCardRequestDto requestDto, HttpServletRequest request) {

        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);

        System.out.println("Owner from jwt: " + username);
        System.out.println(requestDto.getCardNumber());
        System.out.println(requestDto.getExpireDate());
        System.out.println(requestDto.getNameOnCard());
        System.out.println(requestDto.getCvv());

        Client owner = clientService.findByUsername(username);


        if (owner == null) return new ResponseEntity<>("Такого клиента не существует",HttpStatus.OK);


        BankCard bankCardToAdd = new BankCard();
        bankCardToAdd.setNameOnCard(requestDto.getNameOnCard());
        bankCardToAdd.setExpireDate(requestDto.getExpireDate());
        bankCardToAdd.setCardNumber(requestDto.getCardNumber());
        bankCardToAdd.setClient(owner);
        bankCardToAdd.setCvv(requestDto.getCvv());


        try {
            bankCardService.addCard(bankCardToAdd);
            return new ResponseEntity<>("Карта успешно добавлена",HttpStatus.OK);
        } catch (CardAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IncorrectCardDataException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("depositFiat")
    public ResponseEntity<String> depositFiat(@RequestBody FiatDepositDto fiatDepositDto, HttpServletRequest request){

        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);



        Client owner = clientService.findByUsername(username);
        if (owner == null) return new ResponseEntity<>("Такого клиента не существует",HttpStatus.OK);

        if (clientService.depositFiat(owner, fiatDepositDto.getAmount())) {
            return new ResponseEntity<>("Фиатный баланс успешно пополнен", HttpStatus.OK);
        } else return new ResponseEntity<>("Некорректная сумма при пополнении баланса", HttpStatus.OK);
    }


    @PostMapping("sendMoney")
    public ResponseEntity<String>  sendMoney(@RequestBody TransactionRequestDto transactionRequestDto, HttpServletRequest request){

        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);


        try {
            transactionService.makeTransaction(transactionRequestDto, username);
            return new ResponseEntity<>("Транзакция успешно проведена", HttpStatus.OK);
        } catch (InsufficientWalletBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (NotSameCryptoInWalletsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (SameClientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalSendAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }








}
