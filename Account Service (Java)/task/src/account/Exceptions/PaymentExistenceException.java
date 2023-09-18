package account.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Error!")
public class PaymentExistenceException extends RuntimeException {
    public PaymentExistenceException() {}
}
