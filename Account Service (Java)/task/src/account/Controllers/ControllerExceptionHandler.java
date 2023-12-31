package account.Controllers;

import account.Exceptions.CustomErrorMessage;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorMessage> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage(),
                request.getDescription(false).substring(4)
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorMessage> handleConstraintViolation(
            ConstraintViolationException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                e.getMessage(),
                request.getDescription(false).substring(4)
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
