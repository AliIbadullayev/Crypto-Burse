package db.service.crypto.service;


import db.service.crypto.dto.TransactionDto;
import db.service.crypto.exception.*;
import db.service.crypto.model.BlockchainNetwork;
import db.service.crypto.model.Client;
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
import java.util.ArrayList;
import java.util.List;

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
    public void makeTransaction(TransactionDto transactionDto, String senderUsername) throws InsufficientBalanceException, WalletNotFoundException, NotSameCryptoException, SameClientException, IllegalWalletPermissionAttemptException, InvalidAmountException {
        String addressTo = transactionDto.getWalletToAddress();
        String addressFrom = transactionDto.getWalletFromAddress();

        Wallet walletFrom = findByAddress(addressFrom);
        Wallet walletTo = findByAddress(addressTo);

        if (walletTo == null){
            throw new WalletNotFoundException(addressTo);
        }
        if (walletFrom == null){
            throw new WalletNotFoundException(addressFrom);
        }



        double amount = transactionDto.getAmount();
        String blockchainNetworkName = transactionDto.getBlockchainNetworkName();
        BlockchainNetwork blockchainNetwork = findByNetworkName(blockchainNetworkName);

        if (checkWallets(walletTo,walletFrom,amount,blockchainNetwork.getFee(),senderUsername)){
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
    private boolean checkWallets(Wallet walletTo, Wallet walletFrom, double amount, double fee, String senderUsername) throws NotSameCryptoException, InsufficientBalanceException, IllegalWalletPermissionAttemptException, SameClientException {


        if (!walletFrom.getCryptoName().equals(walletTo.getCryptoName())){
            throw new NotSameCryptoException("Кошельки соответствуют разным криптовалютам!");
        }

        if (!checkBalance(walletFrom,amount,fee)) {
            throw new InsufficientBalanceException("Недостаточно средств на балансе кошелька отправителя!");
        }

        if (!walletFrom.getClient().getUserLogin().equals(senderUsername)){
            throw new IllegalWalletPermissionAttemptException("Вы не можете отправить средства не со своего кошелька!");
        }

        if (walletFrom.getClient().getUserLogin().equals(walletTo.getClient().getUserLogin())){
            throw new SameClientException("Вы не можете отправить средства самому себе!");
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


    public List<TransactionDto> getClientTransactions(Client client) {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDto> transactionDtos = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getWalletFrom().getClient() == client)
                transactionDtos.add(TransactionDto.fromTransaction(transaction));
        }

        return transactionDtos;

    }

    public List<BlockchainNetwork> getBlockchainNetworks() {
        return blockchainNetworkRepository.findAll();
    }
}
