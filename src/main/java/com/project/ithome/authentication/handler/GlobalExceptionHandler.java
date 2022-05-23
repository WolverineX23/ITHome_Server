package com.project.ithome.authentication.handler;

import com.project.ithome.dto.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)

    public ResponseEntity<RestError> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        RestError error = new RestError();
        StringBuilder message = new StringBuilder();
        for (ObjectError er : ex.getBindingResult().getAllErrors()) {
            message.append(er.getDefaultMessage());
        }
        error.setMessage(message.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
