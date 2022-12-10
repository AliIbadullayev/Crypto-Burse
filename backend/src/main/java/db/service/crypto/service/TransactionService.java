package db.service.crypto.service;


import db.service.crypto.dto.TransactionRequestDto;
import db.service.crypto.exception.InsufficientWalletBalanceException;
import db.service.crypto.exception.NotSameCryptoInWalletsException;
import db.service.crypto.exception.WalletNotFoundException;
import db.service.crypto.model.BlockchainNetwork;
import db.service.crypto.model.Transaction;
import db.service.crypto.model.Wallet;
import db.service.crypto.repository.BlockchainNetworkRepository;
import db.service.crypto.repository.TransactionRepository;
import db.service.crypto.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Slf4j
public class TransactionService {
    private final WalletRepository walletRepository;
    private final BlockchainNetworkRepository blockchainNetworkRepository;

    private final TransactionRepository transactionRepository;

    private final WalletService walletService;


    @Autowired
    public TransactionService(WalletRepository walletRepository, BlockchainNetworkRepository blockchainNetworkRepository, TransactionRepository transactionRepository, WalletService walletService) {
        this.walletRepository = walletRepository;
        this.blockchainNetworkRepository = blockchainNetworkRepository;
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
    }

    @Transactional
    public void makeTransaction(TransactionRequestDto transactionRequestDto) throws InsufficientWalletBalanceException, WalletNotFoundException, NotSameCryptoInWalletsException {
        System.out.println(transactionRequestDto.getWalletFromAddress());
        System.out.println(transactionRequestDto.getWalletToAddress());
        System.out.println(transactionRequestDto.getAmount());
        System.out.println(transactionRequestDto.getBlockchainNetworkName());

        String addressTo = transactionRequestDto.getWalletToAddress();
        String addressFrom = transactionRequestDto.getWalletFromAddress();

        Wallet walletTo = findByAddress(addressTo);
        Wallet walletFrom = findByAddress(addressFrom);

        if (walletTo == null){
            throw new WalletNotFoundException(addressTo);
        }
        if (walletFrom == null){
            throw new WalletNotFoundException(addressFrom);
        }



        double amount = transactionRequestDto.getAmount();
        String blockchainNetworkName = transactionRequestDto.getBlockchainNetworkName();
        BlockchainNetwork blockchainNetwork = findByNetworkName(blockchainNetworkName);

        if (checkWallets(walletTo,walletFrom,amount,blockchainNetwork.getFee())){
            walletService.depositWallet(walletTo, amount-blockchainNetwork.getFee());
            walletService.withdrawFromWallet(walletFrom,amount);
            Timestamp transactionTimestamp = new Timestamp(System.currentTimeMillis()+blockchainNetwork.getLeadTime());
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setWalletFrom(walletFrom);
            transaction.setWalletTo(walletTo);
            transaction.setBlockchainNetwork(blockchainNetwork);
            transaction.setTimestamp(transactionTimestamp);
            transactionRepository.save(transaction);
        }






    }
    private boolean checkWallets(Wallet walletTo, Wallet walletFrom, double amount, double fee) throws NotSameCryptoInWalletsException, InsufficientWalletBalanceException {


        if (!walletFrom.getCrypto_name().equals(walletTo.getCrypto_name())){
            throw new NotSameCryptoInWalletsException("Кошельки соответствуют разным криптовалютам!");
        }

        if (!checkBalance(walletFrom,amount,fee)) {
            throw new InsufficientWalletBalanceException("Недостаточно средств на балансе кошелька отправителя!");
        }

        return true;
    }

    private boolean checkBalance(Wallet walletFrom, double amount, double fee){
        return (walletFrom.getAmount() >= (amount+fee));
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

    public BlockchainNetwork findByNetworkName(String name){
        BlockchainNetwork result = null;
        result = blockchainNetworkRepository.findByName(name);

        if (result == null){
            log.info("IN findByNetworkName - no blockchainNetwork found by networkName: {}",name);
            return null;
        }

        log.info("IN findByNetworkName - blockchainNetwork: {} found by networkName: {}",result,name);
        return result;
    }


}
