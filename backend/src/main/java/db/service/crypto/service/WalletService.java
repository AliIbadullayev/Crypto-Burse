package db.service.crypto.service;

import db.service.crypto.dto.*;
import db.service.crypto.exception.*;
import db.service.crypto.model.*;
import db.service.crypto.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletService {

    private static final double STACKING_INTEREST_RATE  = 1.2;
    private final WalletRepository walletRepository;
    private final ClientRepository clientRepository;
    private final CryptoRepository cryptoRepository;
    private final StackingRepository stackingRepository;
    private final FiatToCryptoRepository fiatToCryptoRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeCryptos(){
        List<Crypto> cryptos = new ArrayList<>();
        cryptos.add(new Crypto("Bitcoin",31286.7));
        cryptos.add(new Crypto("Ethereum",12034.75));
        cryptos.add(new Crypto("Litecoin",280.9));
        cryptos.add(new Crypto("Tether",1.01));
        cryptos.add(new Crypto("Shibacoin",0.000009927));
        cryptoRepository.saveAll(cryptos);
    }

    public void createWalletsForUser(Client client){
        List<Crypto> cryptoList;
        cryptoList = cryptoRepository.findAll();

        for (int i = 0; i < cryptoList.size(); i++) {
            Wallet wallet = new Wallet();
            wallet.setAddress(generateId());
            wallet.setCryptoName(cryptoList.get(i).getName());
            wallet.setClient(client);
            walletRepository.save(wallet);
        }
    }

    public String generateId(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }


    @Transactional
    public void fiatToCrypto(FiatToCryptoDto fiatToCryptoDto, String username) throws WalletNotFoundException, IllegalWalletPermissionAttemptException, CryptoNotFoundException, InvalidAmountException, InsufficientBalanceException {
        String walletAddress = fiatToCryptoDto.getWalletAddress();

        Wallet wallet = findByAddress(walletAddress);

        if (wallet == null) throw new WalletNotFoundException(walletAddress);
        if (!wallet.getClient().getUserLogin().equals(username)) throw new IllegalWalletPermissionAttemptException("Кошелёк должен принадлежать вам!");


        double amount = fiatToCryptoDto.getAmount();
        Crypto crypto = findByCryptoName(wallet.getCryptoName());

        if (crypto == null) throw new CryptoNotFoundException("Данная криптовалюта не найдена!");

        double amountInCrypto = amount/crypto.getExchange_rate();



        withdrawFiat(username,amount);
        depositWallet(wallet,amountInCrypto);

        FiatToCrypto fiatToCrypto = new FiatToCrypto();

        fiatToCrypto.setWallet(wallet);
        fiatToCrypto.setAmount(amount);
        fiatToCrypto.setTimestamp(new Timestamp(System.currentTimeMillis()));

        fiatToCryptoRepository.save(fiatToCrypto);
    }

    @Transactional
    public StackingDto toStake(StackingRequestDto stackingRequestDto, String username) throws WalletNotFoundException, IllegalWalletPermissionAttemptException, InvalidAmountException, InsufficientBalanceException, StakingIsAlreadyExistException, IncorrectStakingDurationException {
        String walletAddress = stackingRequestDto.getWalletAddress();

        Wallet wallet = findByAddress(walletAddress);

        if (wallet == null) throw new WalletNotFoundException(walletAddress);
        if (!wallet.getClient().getUserLogin().equals(username)) throw new IllegalWalletPermissionAttemptException("Кошелёк должен принадлежать вам!");

        double amount = stackingRequestDto.getAmount();

        if (!checkBalance(wallet,amount)) throw new InsufficientBalanceException("Недостаточно средств на балансе кошелька!");

        int years = stackingRequestDto.getYears();
        long yearsInMilliseconds;

        if (years == 1 || years == 2 || years == 3 || years == 4 || years == 5){
            yearsInMilliseconds = (long) years * 365 * 24 * 60 * 60 * 1000;
        } else throw new IncorrectStakingDurationException("Некорректно указан период хранения");



        Stacking testStaking = stackingRepository.findById(walletAddress).orElse(null);

        if (testStaking != null) throw new StakingIsAlreadyExistException("Стейкинг по этому кошельку уже существует!");

        withdrawFromWallet(wallet, amount);
        Stacking stacking = new Stacking();
        stacking.setAmount(amount);
        stacking.setWalletAddress(walletAddress);
        stacking.setInterestRate(STACKING_INTEREST_RATE);
        stacking.setExpireDate(new Timestamp(System.currentTimeMillis()+yearsInMilliseconds));
        stackingRepository.save(stacking);
        return StackingDto.fromStacking(stacking);
    }

    @Transactional
    public void freeStake(StackingDto stackingDto, String username) throws WalletNotFoundException, IllegalWalletPermissionAttemptException, StakeIsNotReadyYetException, StakingNotFoundException, InvalidAmountException {
        String walletAddress = stackingDto.getWalletAddress();

        Wallet wallet = findByAddress(walletAddress);

        if (wallet == null) throw new WalletNotFoundException(walletAddress);
        if (!wallet.getClient().getUserLogin().equals(username)) throw new IllegalWalletPermissionAttemptException("Кошелёк должен принадлежать вам!");

        Stacking stackingToFree = stackingRepository.findById(walletAddress).orElse(null);

        if (stackingToFree == null) throw new StakingNotFoundException("У вас нет активных депозитов по этому кошельку");

        if (stackingToFree.getExpireDate().compareTo(new Timestamp(System.currentTimeMillis())) > 0)
            throw new StakeIsNotReadyYetException("Депозит ещё рано выводить!");

        depositWallet(wallet,stackingToFree.getAmount()*stackingToFree.getInterestRate());
        stackingRepository.delete(stackingToFree);
    }

    private boolean checkBalance(Wallet wallet, double amount){
        return (wallet.getAmount() >= amount);
    }

    public boolean depositWallet(Wallet wallet,double amount) throws InvalidAmountException {
        if (amount>0){
            double amountBefore = wallet.getAmount();
            double amountAfter = amountBefore+amount;
            wallet.setAmount(amountAfter);
            walletRepository.save(wallet);
            log.info("Deposit wallet with address {}. Amount before: {}. Amount after: {}",wallet.getAddress(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Сумма транзакции не может быть отрицательной");
    }

    public boolean withdrawFromWallet(Wallet wallet, double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount>0){
            double amountBefore = wallet.getAmount();
            double amountAfter = amountBefore-amount;
            if (amountAfter < 0) throw new InsufficientBalanceException("На балансе кошелька недостаточно средств");
            wallet.setAmount(amountAfter);
            walletRepository.save(wallet);
            log.info("Withdraw wallet with address {}. Amount before: {}. Amount after: {}",wallet.getAddress(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Сумма транзакции не может быть отрицательной");
    }

    public Wallet findByAddress(String address){
        Wallet result = null;
        result = walletRepository.findByAddress(address);

        if (result == null){
            log.info("IN findByAddress - no wallet found by walletAddress: {}",address);
            return null;
        }

        log.info("IN findByAddress - wallet: {} found by walletAddress: {}",result,address);
        return result;
    }


    public Crypto findByCryptoName(String name){
        Crypto result = null;
        result = cryptoRepository.findByName(name);

        if (result == null){
            log.info("IN findByCryptoName - no crypto found by cryptoName: {}",name);
            return null;
        }

        log.info("IN findByCryptoName - crypto: {} found by cryptoName: {}",result,name);
        return result;
    }

    public boolean withdrawFiat(String username, double amount) throws InvalidAmountException, InsufficientBalanceException {

        Client client = clientRepository.findByUserLogin(username);


        if (amount > 0){
            if (client.getFiatBalance()<amount) throw new InsufficientBalanceException("Не достаточно средств на фиатном счёте клиента");
            double amountBefore = client.getFiatBalance();
            double amountAfter = amountBefore - amount;
            client.setFiatBalance(amountAfter);
            clientRepository.save(client);
            log.info("Reduce fiatBalance in client with username {}. Amount before: {}. Amount after: {}",client.getUserLogin(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Сумма транзакции не может быть отрицательной");

    }

    public boolean depositFiat(String username,double amount) throws InvalidAmountException {

        Client client = clientRepository.findByUserLogin(username);

        if (amount > 0 && amount <= 1000000000) {
            double amountBefore = client.getFiatBalance();
            double amountAfter = amountBefore + amount;
            client.setFiatBalance(amountAfter);
            clientRepository.save(client);
            log.info("Add fiatBalance in client with username {}. Amount before: {}. Amount after: {}",client.getUserLogin(),amountBefore,amountAfter);
            return true;
        } else throw new InvalidAmountException("Некорректная сумма пополнения");
    }

    public WalletDto getClientWallet (String walletAddress){
        Wallet wallet = walletRepository.findByAddress(walletAddress);
        return WalletDto.fromWallet(wallet);
    }

    public List<WalletDto> getAllClientWallets(Client client){
        List<Wallet> wallets = walletRepository.findAll();
        List<WalletDto> walletDtos = new ArrayList<>();
        for (Wallet wallet : wallets) {
            if (wallet.getClient() == client) walletDtos.add(WalletDto.fromWallet(wallet));
        }
        return walletDtos;
    }


    public List<FiatToCryptoDto> getClientFiatToCryptos(Client client) {
        List<FiatToCrypto> fiatToCryptos = fiatToCryptoRepository.findAll();
        List<FiatToCryptoDto> fiatToCryptoDtos = new ArrayList<>();
        for (FiatToCrypto fiatToCrypto : fiatToCryptos) {
            if (fiatToCrypto.getWallet().getClient() == client) fiatToCryptoDtos.add(FiatToCryptoDto.fromFiatToCrypto(fiatToCrypto));
        }
        return fiatToCryptoDtos;
    }

    public StackingDto getWalletStaking(String walletAddress) throws NoSuchWalletException, StakingNotFoundException {
        Wallet wallet = walletRepository.findByAddress(walletAddress);
        if (wallet == null) throw new NoSuchWalletException("Кошелёк с таким адресом не найден!");
        Stacking stacking = stackingRepository.findById(walletAddress).orElse(null);
        if (stacking != null) return StackingDto.fromStacking(stacking);
        else throw new StakingNotFoundException("По такому адресу не найден депозит");
    }
}
