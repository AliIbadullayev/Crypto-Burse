package db.service.crypto.exception.handler;

import db.service.crypto.exception.AlreadyScoredException;
import db.service.crypto.exception.CardAlreadyExistException;
import db.service.crypto.exception.CryptoNotFoundException;
import db.service.crypto.exception.IllegalWalletPermissionAttemptException;
import db.service.crypto.exception.IncorrectCardDataException;
import db.service.crypto.exception.IncorrectStakingDurationException;
import db.service.crypto.exception.InsufficientBalanceException;
import db.service.crypto.exception.InvalidAmountException;
import db.service.crypto.exception.InvalidOperationTypeException;
import db.service.crypto.exception.JwtAuthenticationException;
import db.service.crypto.exception.JwtTokenIsEmptyException;
import db.service.crypto.exception.NftNotFoundException;
import db.service.crypto.exception.NftOwnerException;
import db.service.crypto.exception.NftPlacingException;
import db.service.crypto.exception.NoSuchP2POfferException;
import db.service.crypto.exception.NoSuchWalletException;
import db.service.crypto.exception.NotSameCryptoException;
import db.service.crypto.exception.SameClientException;
import db.service.crypto.exception.SameCryptoInWalletsException;
import db.service.crypto.exception.StakeIsNotReadyYetException;
import db.service.crypto.exception.StakingIsAlreadyExistException;
import db.service.crypto.exception.StakingNotFoundException;
import db.service.crypto.exception.UserAlreadyExistException;
import db.service.crypto.exception.UserNotFoundException;
import db.service.crypto.exception.WalletNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({JwtAuthenticationException.class, SignatureException.class, ExpiredJwtException.class,
            AuthenticationException.class})
    public ResponseEntity<?> handleAccessException(Exception ex) {
        log.warn("Access exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtTokenIsEmptyException.class)
    public ResponseEntity<?> handleUnauthorizedException(Exception ex) {
        log.warn("Access exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UserNotFoundException.class, WalletNotFoundException.class, StakingNotFoundException.class,
    CryptoNotFoundException.class, NftNotFoundException.class, NoSuchWalletException.class, NoSuchP2POfferException.class})
    public ResponseEntity<?> handleNotFoundException(Exception ex) {
        log.warn("Not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CardAlreadyExistException.class, IncorrectCardDataException.class, InvalidAmountException.class,
            InsufficientBalanceException.class, NotSameCryptoException.class,
            SameClientException.class, IllegalWalletPermissionAttemptException.class, SameCryptoInWalletsException.class,
            AlreadyScoredException.class, NftPlacingException.class, StakingIsAlreadyExistException.class,
            IncorrectStakingDurationException.class, StakeIsNotReadyYetException.class, InvalidOperationTypeException.class,
            NftOwnerException.class, UserAlreadyExistException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception ex) {
        log.warn("Bad Request: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
