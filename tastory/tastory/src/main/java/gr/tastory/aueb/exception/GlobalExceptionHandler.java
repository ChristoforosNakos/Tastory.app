package gr.tastory.aueb.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotAuthorizedException.class)
    public String handleNotAuthorized() {
        return "error-403";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound() {
        return "error-404";
    }
}