package db.service.crypto.rest;

import db.service.crypto.dto.*;
import db.service.crypto.exception.*;
import db.service.crypto.model.BankCard;
import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.*;
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

    private final WalletService walletService;

    private final BankCardService bankCardService;

    private final TransactionService transactionService;

    private final ExchangeService exchangeService;

    private final NftService nftService;

    @Autowired
    public UserRestControllerV1(JwtTokenProvider jwtTokenProvider, UserService userService, ClientService clientService, WalletService walletService, BankCardService bankCardService, TransactionService transactionService, ExchangeService exchangeService, NftService nftService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientService = clientService;
        this.walletService = walletService;
        this.bankCardService = bankCardService;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
        this.nftService = nftService;
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


    @PostMapping("sendCrypto")
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
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (NotSameCryptoInWalletsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (SameClientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalWalletPermissionAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }


    @PostMapping("exchangeCrypto")
    public ResponseEntity<String> exchangeCrypto(@RequestBody ExchangeRequestDto exchangeRequestDto, HttpServletRequest request){
        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);


        try {
            exchangeService.makeExchange(exchangeRequestDto,username);
            return new ResponseEntity<>("Обмен успешно проведён", HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (SameCryptoInWalletsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalWalletPermissionAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }


    }


    @PostMapping("fiatToCrypto")
    public ResponseEntity<String> fiatToCrypto(@RequestBody FiatToCryptoRequestDto fiatToCryptoRequestDto, HttpServletRequest request){
        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);

        try {
            walletService.fiatToCrypto(fiatToCryptoRequestDto,username);
            return new ResponseEntity<>("Обмен успешно проведён", HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalWalletPermissionAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (CryptoNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }


    @PostMapping("scoreNft")
    public ResponseEntity<String> likeNft(@RequestBody ScoreNftRequestDto scoreNftRequestDto, HttpServletRequest request){
        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);


        try {
            nftService.scoreNft(scoreNftRequestDto, username);
            return new ResponseEntity<>("NFT успешно оценена", HttpStatus.OK);
        } catch (NftNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (NftIsNotPlacedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (AlreadyScoredException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }








}
