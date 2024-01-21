package br.com.projeto.demoparkapi.web.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.projeto.demoparkapi.exception.EntityNotFoundException;
import br.com.projeto.demoparkapi.exception.PasswordException;
import br.com.projeto.demoparkapi.exception.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(
        MethodArgumentNotValidException ex, 
        HttpServletRequest request,
        BindingResult result) {

            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

            log.error("Api Error - ", ex);
            return ResponseEntity
            .status(status)
            .body(new ErrorMessage(request, status, "Campo(s) Inválido(s)", result));
    }

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(
        RuntimeException ex,
        HttpServletRequest request) {

            HttpStatus status = HttpStatus.CONFLICT;

            log.error("Api Error - ", ex);
            return ResponseEntity
            .status(status)
            .body(new ErrorMessage(request, status, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex, HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;

        log.error("Api Error - ", ex);
        return ResponseEntity
        .status(status)
        .body(new ErrorMessage(request, status, ex.getMessage()));
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorMessage> passwordException(RuntimeException ex, HttpServletRequest request){

        HttpStatus status = HttpStatus.FORBIDDEN;

        log.error("Api Error - ", ex);
        return ResponseEntity
        .status(status)
        .body(new ErrorMessage(request, status, ex.getMessage()));
    }
}
