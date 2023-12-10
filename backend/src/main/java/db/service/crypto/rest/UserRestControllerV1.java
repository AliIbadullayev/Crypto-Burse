package db.service.crypto.rest;

import db.service.crypto.dto.*;
import db.service.crypto.exception.*;
import db.service.crypto.model.*;
import db.service.crypto.security.jwt.JwtTokenProvider;
import db.service.crypto.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "addCard")
    public ResponseEntity<String> addCard(@RequestBody BankCardDto requestDto, HttpServletRequest request) {
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

