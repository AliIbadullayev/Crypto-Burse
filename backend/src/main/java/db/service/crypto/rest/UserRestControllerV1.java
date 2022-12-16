package db.service.crypto.rest;

import db.service.crypto.dto.*;
import db.service.crypto.exception.*;
import db.service.crypto.model.BankCard;
import db.service.crypto.model.Client;
import db.service.crypto.model.P2PTransaction;
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

    private final P2PService p2pService;

    @Autowired
    public UserRestControllerV1(JwtTokenProvider jwtTokenProvider, UserService userService, ClientService clientService, WalletService walletService, BankCardService bankCardService, TransactionService transactionService, ExchangeService exchangeService, NftService nftService, P2PService p2pService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientService = clientService;
        this.walletService = walletService;
        this.bankCardService = bankCardService;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
        this.nftService = nftService;
        this.p2pService = p2pService;
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
    public ResponseEntity<String> addCard(@RequestBody BankCardDto requestDto, HttpServletRequest request) {

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
    public ResponseEntity<String>  sendMoney(@RequestBody TransactionDto transactionDto, HttpServletRequest request){

        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);


        try {
            transactionService.makeTransaction(transactionDto, username);
            return new ResponseEntity<>("Транзакция успешно проведена", HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (NotSameCryptoException e) {
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
    public ResponseEntity<String> exchangeCrypto(@RequestBody ExchangeDto exchangeDto, HttpServletRequest request){
        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);


        try {
            exchangeService.makeExchange(exchangeDto,username);
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
    public ResponseEntity<String> fiatToCrypto(@RequestBody FiatToCryptoDto fiatToCryptoDto, HttpServletRequest request){
        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);

        try {
            walletService.fiatToCrypto(fiatToCryptoDto,username);
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

    @PostMapping("toStake")
    public ResponseEntity<?> toStake(@RequestBody StackingDto stackingDto, HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        try {
            walletService.toStake(stackingDto,username);
            return new ResponseEntity<>("Криптовалюта успешно помещена в стейкинг!", HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalWalletPermissionAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (StakingIsAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("freeStake")
    public ResponseEntity<?> freeStake(@RequestBody StackingDto stackingDto, HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        try {
            walletService.freeStake(stackingDto,username);
            return new ResponseEntity<>("Стейкинг успешно вернул вам дивиденды!", HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalWalletPermissionAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (StakeIsNotReadyYetException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (StakingNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("postOffer")
    public ResponseEntity<?> postOffer(@RequestBody P2PDto p2pDto, HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        try {
            p2pDto = p2pService.postOffer(p2pDto,username);
            return new ResponseEntity<>(p2pDto, HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (IllegalWalletPermissionAttemptException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (CryptoNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidOperationTypeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (NotSameCryptoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("respondToOffer")
    public ResponseEntity<?> respondToOffer(@RequestBody P2PDto p2pDto, HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        try {
            p2pDto = p2pService.respondToOffer(p2pDto,client);
            return new ResponseEntity<>(p2pDto, HttpStatus.OK);
        } catch (NoSuchP2POfferException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (NoSuchWalletException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InvalidAmountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (SameClientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("getAllOffers")
    public ResponseEntity<?> getAllOffers(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);


        return new ResponseEntity<>(p2pService.getAllOffers(), HttpStatus.OK);
    }

    @GetMapping("getAllMyP2P")
    public ResponseEntity<?> getAllMyP2P(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);


        return new ResponseEntity<>(p2pService.getAllClientP2P(client), HttpStatus.OK);
    }




    @GetMapping("getClientInfo")
    public ResponseEntity<?> getCurrentClientInfo(HttpServletRequest request){
        String username = null;

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);

        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);

        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);

        Client client = clientService.findByUsername(username);

        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        ClientInfoDto result = ClientInfoDto.fromUser(client);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("getAllClientWallets")
    public ResponseEntity<?> getAllClentWallets(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(walletService.getAllClientWallets(client), HttpStatus.OK);

    }

    @GetMapping("getAllClientBankCards")
    public ResponseEntity<?> getAllClientBankCards(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(bankCardService.getAllClientCards(client), HttpStatus.OK);
    }

    @GetMapping("getAllClientNfts")
    public ResponseEntity<?> getAllClientNfts(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(nftService.getAllClientNfts(username), HttpStatus.OK);
    }

    @GetMapping("getAllNfts")
    public ResponseEntity<?> getAllNfts(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(nftService.getAllNfts(), HttpStatus.OK);
    }



    @GetMapping("getClientTransactions")
    public ResponseEntity<?> getClientTransactions(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(transactionService.getClientTransactions(client), HttpStatus.OK);
    }

    @GetMapping("getClientExchanges")
    public ResponseEntity<?> getClientExchanges(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(exchangeService.getClientExchanges(client), HttpStatus.OK);
    }

    @GetMapping("getClientFiatToCryptos")
    public ResponseEntity<?> getClientFiatToCryptos(HttpServletRequest request){
        String username = null;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null){
            username = jwtTokenProvider.getUsername(token);
        } else return new ResponseEntity<>("Токен пуст!", HttpStatus.OK);
        if (username == null) return new ResponseEntity<>("Пользователь по данному токену не найден!!", HttpStatus.OK);
        Client client = clientService.findByUsername(username);
        if (client == null) return new ResponseEntity<>("Не удалось найти такого пользователя", HttpStatus.OK);

        return new ResponseEntity<>(walletService.getClientFiatToCryptos(client), HttpStatus.OK);
    }














}
