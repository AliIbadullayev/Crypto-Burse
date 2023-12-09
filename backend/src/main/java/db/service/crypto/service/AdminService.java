package db.service.crypto.service;

import db.service.crypto.dto.AdminDecisionDto;
import db.service.crypto.dto.NftDto;
import db.service.crypto.dto.P2PDto;
import db.service.crypto.exception.InvalidAmountException;
import db.service.crypto.exception.NoSuchP2POfferException;
import db.service.crypto.model.Admin;
import db.service.crypto.model.OperationType;
import db.service.crypto.model.P2PTransaction;
import db.service.crypto.model.P2PTransactionStatus;
import db.service.crypto.repository.AdminRepository;
import db.service.crypto.repository.P2PRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;

    private final P2PRepository p2pRepository;

    private final WalletService walletService;

    @Autowired
    public AdminService(AdminRepository adminRepository, P2PRepository p2pRepository, WalletService walletService) {
        this.adminRepository = adminRepository;
        this.p2pRepository = p2pRepository;
        this.walletService = walletService;
    }

    public Optional<Admin> findByUsername(String username) {
        Admin result = adminRepository.findById(username).orElse(null);
        if (result == null){
            log.info("IN findByUsername - no admin found by username: {}",username);
            return Optional.empty();
        }
        log.info("IN findByUsername - admin: {} found by username: {}",result,username);
        return Optional.of(result);
    }

    public P2PDto makeDecision(AdminDecisionDto adminDecisionDto, Admin admin) throws NoSuchP2POfferException, InvalidAmountException {
        P2PTransaction p2pTransaction = p2pRepository.findById(adminDecisionDto.getP2pTransactionId()).orElse(null);

        if (p2pTransaction == null || !p2pTransaction.getP2pTransactionStatus().equals(P2PTransactionStatus.ADMIN_WAITING)) throw new NoSuchP2POfferException("Не найдена транзакция для подтверждения админом с таким Id!");

        double cryptoAmount = p2pTransaction.getCryptoAmount();
        double fiatAmount = p2pTransaction.getFiatAmount();


        if (adminDecisionDto.isApproved()){
            if (p2pTransaction.getOperationType().equals(OperationType.BUY_CRYPTO)){
                walletService.depositWallet(p2pTransaction.getWalletOne(),cryptoAmount);
                walletService.depositFiat(p2pTransaction.getWalletTwo().getClient().getUserLogin(),fiatAmount);

            } else {
                walletService.depositWallet(p2pTransaction.getWalletTwo(),cryptoAmount);
                walletService.depositFiat(p2pTransaction.getWalletOne().getClient().getUserLogin(),fiatAmount);
            }
            p2pTransaction.setP2pTransactionStatus(P2PTransactionStatus.APPROVED);
            p2pTransaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
            p2pTransaction.setAdmin(admin);
            p2pRepository.save(p2pTransaction);
        } else {
            if (p2pTransaction.getOperationType().equals(OperationType.BUY_CRYPTO)){
                walletService.depositWallet(p2pTransaction.getWalletTwo(),cryptoAmount);
                walletService.depositFiat(p2pTransaction.getWalletOne().getClient().getUserLogin(),fiatAmount);

            } else {
                walletService.depositWallet(p2pTransaction.getWalletOne(),cryptoAmount);
                walletService.depositFiat(p2pTransaction.getWalletTwo().getClient().getUserLogin(),fiatAmount);
            }
            p2pTransaction.setP2pTransactionStatus(P2PTransactionStatus.REJECTED);
            p2pTransaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
            p2pTransaction.setAdmin(admin);
            p2pRepository.save(p2pTransaction);
        }


        return P2PDto.fromP2PTransaction(p2pTransaction);
    }




}
