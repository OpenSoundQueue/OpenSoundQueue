/**
 * This class handles certain exceptions that might be thrown at runtime
 */

package com.example.backend.exceptions;

import com.example.backend.ResponseDtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This exception is being thrown in requests that require authorization
     * @param ex exception that is to be caught
     * @return http response to give appropriate error messages
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleInvalidParameterException(UnauthorizedException ex) {
        if (ex.getMessage().equals("token")) {
            return new ResponseEntity<>(new ErrorDto("Invalid API Key"), HttpStatus.UNAUTHORIZED);
        } else if (ex.getMessage().equals("permission")){
            return new ResponseEntity<>(new ErrorDto("Insufficient Permissions"), HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}