package db.service.crypto.rest;

import db.service.crypto.dto.AddCardRequestDto;
import db.service.crypto.dto.FiatDepositDto;
import db.service.crypto.dto.UserDto;
import db.service.crypto.exception.CardAlreadyExistException;
import db.service.crypto.exception.IncorrectCardDataException;
import db.service.crypto.model.BankCard;
import db.service.crypto.model.Client;
import db.service.crypto.model.User;
import db.service.crypto.service.BankCardService;
import db.service.crypto.service.ClientService;
import db.service.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/users/")
public class UserRestControllerV1 {

    private final UserService userService;

    private final ClientService clientService;

    private final BankCardService bankCardService;

    @Autowired
    public UserRestControllerV1(UserService userService, ClientService clientService, BankCardService bankCardService) {
        this.userService = userService;
        this.clientService = clientService;
        this.bankCardService = bankCardService;
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


//   TODO Сделать так, чтобы имя клиента бралось из жвт токена, а не отправлялось напрямую, иначе один клиент может другому карту добавить, либо на фронте автоматом отправлять имя клиента вместе с остальной инфой
    @PostMapping(value = "addCard")
    public ResponseEntity<String> addCard(@RequestBody AddCardRequestDto requestDto) {

        System.out.println(requestDto.getCardNumber());
        System.out.println(requestDto.getExpireDate());
        System.out.println(requestDto.getNameOnCard());
        System.out.println(requestDto.getCvv());
        System.out.println(requestDto.getClientLogin());

        Client owner = clientService.findByUsername(requestDto.getClientLogin().trim());


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
        } catch (CardAlreadyExistException | IncorrectCardDataException e) {
            return new ResponseEntity<>("Карта уже добавлена либо введены некорректные данные!", HttpStatus.OK);
        }
    }

    @PostMapping("depositFiat")
    public ResponseEntity<String> depositFiat(@RequestBody FiatDepositDto fiatDepositDto){
        Client owner = clientService.findByUsername(fiatDepositDto.getClientLogin().trim());
        if (owner == null) return new ResponseEntity<>("Такого клиента не существует",HttpStatus.OK);

        if (clientService.depositFiat(owner, fiatDepositDto.getAmount())) {
            return new ResponseEntity<>("Фиатный баланс успешно пополнен", HttpStatus.OK);
        } else return new ResponseEntity<>("Некорректная сумма при пополнении баланса", HttpStatus.OK);
    }



}
