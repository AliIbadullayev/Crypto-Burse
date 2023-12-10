package db.service.crypto.rest;

import db.service.crypto.dto.BankCardDto;
import db.service.crypto.dto.ClientInfoDto;
import db.service.crypto.dto.ExchangeDto;
import db.service.crypto.dto.FiatDepositDto;
import db.service.crypto.dto.FiatToCryptoDto;
import db.service.crypto.dto.NftDto;
import db.service.crypto.dto.P2PDto;
import db.service.crypto.dto.ScoreNftRequestDto;
import db.service.crypto.dto.StackingDto;
import db.service.crypto.dto.StackingRequestDto;
import db.service.crypto.dto.TransactionDto;
import db.service.crypto.dto.UserDto;
import db.service.crypto.exception.JwtTokenIsEmptyException;
import db.service.crypto.exception.UserNotFoundException;
import db.service.crypto.model.BankCard;
import db.service.crypto.model.Client;
import db.service.crypto.model.Crypto;
import db.service.crypto.model.User;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.BankCardService;
import db.service.crypto.service.ClientService;
import db.service.crypto.service.ExchangeService;
import db.service.crypto.service.NftService;
import db.service.crypto.service.P2PService;
import db.service.crypto.service.TransactionService;
import db.service.crypto.service.UserService;
import db.service.crypto.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users/")
public class UserRestControllerV1 {

    private final NftService nftService;
    private final P2PService p2pService;
    private final UserService userService;
    private final WalletService walletService;
    private final ClientService clientService;
    private final BankCardService bankCardService;
    private final ExchangeService exchangeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TransactionService transactionService;

    @GetMapping(value = "{username}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "username") String username) {
        log.info("GET --> /api/v1/users/{}", username);
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "addCard")
    public ResponseEntity<String> addCard(@RequestBody BankCardDto requestDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/addCard");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        BankCard bankCardToAdd = BankCard.builder()
                .nameOnCard(requestDto.getNameOnCard())
                .expireDate(requestDto.getExpireDate())
                .cardNumber(requestDto.getCardNumber())
                .client(client)
                .cvv(requestDto.getCvv())
                .build();
        bankCardService.addCard(bankCardToAdd);
        return new ResponseEntity<>("Карта успешно добавлена", HttpStatus.OK);
    }

    @PostMapping("depositFiat")
    public ResponseEntity<String> depositFiat(@RequestBody FiatDepositDto fiatDepositDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/depositFiat");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        walletService.depositFiat(username, fiatDepositDto.getAmount());
        return new ResponseEntity<>("Транзакция успешно проведена", HttpStatus.OK);
    }

    @GetMapping("getClientWallet")
    public ResponseEntity<?> getClientWallet(@RequestParam(name = "address") String walletAddress,
                                             HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getClientWallet");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(walletService.getClientWallet(walletAddress), HttpStatus.OK);
    }

    @PostMapping("sendCrypto")
    public ResponseEntity<String> sendMoney(@RequestBody TransactionDto transactionDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/sendCrypto");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        transactionService.makeTransaction(transactionDto, username);
        return new ResponseEntity<>("Транзакция успешно проведена", HttpStatus.OK);
    }

    @PostMapping("exchangeCrypto")
    public ResponseEntity<String> exchangeCrypto(@RequestBody ExchangeDto exchangeDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/exchangeCrypto");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        exchangeService.makeExchange(exchangeDto, username);
        return new ResponseEntity<>("Обмен успешно проведён", HttpStatus.OK);
    }

    @PostMapping("fiatToCrypto")
    public ResponseEntity<String> fiatToCrypto(@RequestBody FiatToCryptoDto fiatToCryptoDto,
                                               HttpServletRequest request) {
        log.info("POST --> /api/v1/users/fiatToCrypto");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        walletService.fiatToCrypto(fiatToCryptoDto, username);
        return new ResponseEntity<>("Обмен успешно проведён", HttpStatus.OK);
    }

    @PostMapping("scoreNft")
    public ResponseEntity<?> likeNft(@RequestBody ScoreNftRequestDto scoreNftRequestDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/scoreNft");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        NftDto nftDto = nftService.scoreNft(scoreNftRequestDto, username);
        return new ResponseEntity<>(nftDto, HttpStatus.OK);
    }

    @PostMapping("toStake")
    public ResponseEntity<?> toStake(@RequestBody StackingRequestDto stackingRequestDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/toStake");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        StackingDto stackingDto = walletService.toStake(stackingRequestDto, username);
        return new ResponseEntity<>(stackingDto, HttpStatus.OK);
    }

    @PostMapping("freeStake")
    public ResponseEntity<?> freeStake(@RequestBody StackingDto stackingDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/freeStake");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        walletService.freeStake(stackingDto, username);
        return new ResponseEntity<>("Стейкинг успешно вернул вам дивиденды!", HttpStatus.OK);
    }

    @PostMapping("postOffer")
    public ResponseEntity<?> postOffer(@RequestBody P2PDto p2pDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/postOffer");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        p2pDto = p2pService.postOffer(p2pDto, username);
        return new ResponseEntity<>(p2pDto, HttpStatus.OK);
    }

    @PostMapping("respondToOffer")
    public ResponseEntity<?> respondToOffer(@RequestBody P2PDto p2pDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/respondToOffer");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        p2pDto = p2pService.respondToOffer(p2pDto, client);
        return new ResponseEntity<>(p2pDto, HttpStatus.OK);
    }

    @PostMapping("buyNft")
    public ResponseEntity<?> buyNft(@RequestBody NftDto nftDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/buyNft");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        nftDto = nftService.buyNft(nftDto, client);
        return new ResponseEntity<>(nftDto, HttpStatus.OK);
    }

    @PostMapping("sellNft")
    public ResponseEntity<?> sellNft(@RequestBody NftDto nftDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/sellNft");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        nftDto = nftService.sellNft(nftDto, client);
        return new ResponseEntity<>(nftDto, HttpStatus.OK);
    }

    @PostMapping("returnNft")
    public ResponseEntity<?> returnNft(@RequestBody NftDto nftDto, HttpServletRequest request) {
        log.info("POST --> /api/v1/users/returnNft");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        nftDto = nftService.returnNft(nftDto, client);
        return new ResponseEntity<>(nftDto, HttpStatus.OK);
    }

    @GetMapping("getCryptos")
    public ResponseEntity<?> getCryptos(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getCryptos");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(clientService.getAllCryptos(), HttpStatus.OK);
    }

    @GetMapping("getBlockchainNetworks")
    public ResponseEntity<?> getBlockchainNetworks(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getBlockchainNetworks");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(transactionService.getBlockchainNetworks(), HttpStatus.OK);
    }

    @GetMapping("getAllOffers")
    public ResponseEntity<?> getAllOffers(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getAllOffers");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(p2pService.getAllOffers(username), HttpStatus.OK);
    }

    @GetMapping("getAllMyP2P")
    public ResponseEntity<?> getAllMyP2P(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getAllMyP2P");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(p2pService.getAllClientP2P(client), HttpStatus.OK);
    }

    @GetMapping("getClientInfo")
    public ResponseEntity<?> getCurrentClientInfo(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getClientInfo");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        ClientInfoDto result = ClientInfoDto.fromUser(client);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("getAllClientWallets")
    public ResponseEntity<?> getAllClientWallets(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getAllClientWallets");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(walletService.getAllClientWallets(client), HttpStatus.OK);
    }

    @GetMapping("getAllClientBankCards")
    public ResponseEntity<?> getAllClientBankCards(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getAllClientBankCards");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(bankCardService.getAllClientCards(client), HttpStatus.OK);
    }

    @GetMapping("getAllClientNfts")
    public ResponseEntity<?> getAllClientNfts(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getAllClientNfts");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(nftService.getAllClientNfts(username), HttpStatus.OK);
    }

    @GetMapping("getAllNfts")
    public ResponseEntity<?> getAllNfts(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getAllNfts");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(nftService.getAllNfts(), HttpStatus.OK);
    }

    @GetMapping("getClientTransactions")
    public ResponseEntity<?> getClientTransactions(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getClientTransactions");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(transactionService.getClientTransactions(client), HttpStatus.OK);
    }

    @GetMapping("getClientExchanges")
    public ResponseEntity<?> getClientExchanges(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getClientExchanges");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(exchangeService.getClientExchanges(client), HttpStatus.OK);
    }

    @GetMapping("getClientFiatToCryptos")
    public ResponseEntity<?> getClientFiatToCryptos(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getClientFiatToCryptos");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        Client client = clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        return new ResponseEntity<>(walletService.getClientFiatToCryptos(client), HttpStatus.OK);
    }

    @GetMapping("getStackingByWallet")
    public ResponseEntity<?> getStackingByWallet(@RequestParam(name = "walletAddress") String wallet,
                                                 HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getStackingByWallet");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        StackingDto stackingDto = walletService.getWalletStaking(wallet);
        return new ResponseEntity<>(stackingDto, HttpStatus.OK);
    }

    @GetMapping("getCryptoExchangeRates")
    public ResponseEntity<?> getCryptoExchangesRate(HttpServletRequest request) {
        log.info("GET --> /api/v1/users/getCryptoExchangeRates");
        String token = jwtTokenProvider.resolveToken(request)
                .orElseThrow(() -> new JwtTokenIsEmptyException("Токен пуст!"));
        String username = jwtTokenProvider.getUsername(token)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким токеном не найден!"));
        clientService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Клиент с таким токеном не найден"));
        List<Crypto> cryptoList = exchangeService.getCryptosExchangeRates();
        return new ResponseEntity<>(cryptoList, HttpStatus.OK);
    }
}

