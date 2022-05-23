package com.project.ithome.exception.user;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class RegisterException extends BaseException {
    private static final long serialVersionUID = -5849501016305063572L;

    public RegisterException() {
        super(HttpStatus.BAD_REQUEST, "Register error:　Database insert.");
    }
}
