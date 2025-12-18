package NotFound.next_campus.global.auth.token.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// TokenException을 잡아서 401로 응답
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handleTokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}