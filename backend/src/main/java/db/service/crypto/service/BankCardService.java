package db.service.crypto.service;

import db.service.crypto.dto.BankCardDto;
import db.service.crypto.exception.CardAlreadyExistException;
import db.service.crypto.exception.IncorrectCardDataException;
import db.service.crypto.model.BankCard;
import db.service.crypto.model.Client;
import db.service.crypto.repository.BankCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankCardService {

    private final BankCardRepository bankCardRepository;

    public void addCard(BankCard cardToAdd) {
        cardToAdd = validateCard(cardToAdd);
        bankCardRepository.save(cardToAdd);
    }

    public BankCard validateCard(BankCard bankCard) throws CardAlreadyExistException, IncorrectCardDataException {

        String cardNumber = bankCard.getCardNumber().trim();
        String nameOnCard = bankCard.getNameOnCard().trim().toUpperCase();
        String expDate = bankCard.getExpireDate().trim();
        String cvv = bankCard.getCvv().trim();



        if (checkCardNumber(cardNumber)) {
            try {
                if (checkExpiry(expDate)) {
                    if (checkName(nameOnCard)) {
                        if (checkCVV(cvv)) {
                            bankCard.setCardNumber(cardNumber);
                            bankCard.setNameOnCard(nameOnCard);
                            bankCard.setExpireDate(expDate);
                            bankCard.setCvv(cvv);
                            return bankCard;
                        } else throw new IncorrectCardDataException("Неверный формат CVV кода на карте");
                    } else throw new IncorrectCardDataException("Неверный формат имени на карте!");
                } else throw new IncorrectCardDataException("Неверный формат даты на карте, либо срок действия истёк!");
            } catch (ParseException e) {
                throw new IncorrectCardDataException("Неверный формат даты на карте, либо срок действия истёк!");
            }
        }

        return null;
    }

    public boolean checkCardNumber(String cardNumber) throws CardAlreadyExistException, IncorrectCardDataException {
        if (cardNumber.length() == 16) {
            if (cardNumber.matches("[0-9]+")){
                if (findByCardName(cardNumber)==null){
                    return true;
                } else throw new CardAlreadyExistException("Карта с таким номером уже добавлена!");
            } else throw new IncorrectCardDataException("Номер карты может содержать только цифры!");
        } else throw new IncorrectCardDataException("Длина номера карты не равна 16!");
    }

    public boolean checkExpiry(String inputDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        simpleDateFormat.setLenient(false);
        Date expiry = simpleDateFormat.parse(inputDate);
        System.out.println(expiry);
        return !expiry.before(new Date());
    }

    public boolean checkName(String nameOnCard){
        String[] cardNameInfo = nameOnCard.split(" ");
        log.info("cardNameInfo length: " + cardNameInfo.length);
        if (cardNameInfo.length!=2) return false;
        return cardNameInfo[0].matches("[a-zA-Z]+") && cardNameInfo[1].matches("[a-zA-Z]+");
    }

    public boolean checkCVV(String cvv){
        return cvv.length() == 3 && cvv.matches("[0-9]+");
    }




    public BankCard findByCardName(String cardNumber) {
        BankCard result = bankCardRepository.findByCardNumber(cardNumber);
        if (result == null){
            log.info("IN findByCardName - no card found by cardNumber: {}",cardNumber);
            return null;
        }
        log.info("IN findByCardName - card: {} found by cardNumber: {}",result,cardNumber);
        return result;
    }

    public List<BankCardDto> getAllClientCards(Client client){
        List<BankCard> cards = bankCardRepository.findAll();
        List<BankCardDto> bankCardDtos = new ArrayList<>();
        for (BankCard card : cards) {
            if (card.getClient() == client) bankCardDtos.add(BankCardDto.fromCard(card));
        }
        return bankCardDtos;
    }

}

