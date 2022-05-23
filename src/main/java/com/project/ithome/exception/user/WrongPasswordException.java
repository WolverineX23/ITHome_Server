package com.project.ithome.exception.user;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends BaseException {
    private static final long serialVersionUID = -687559333528616962L;

    public WrongPasswordException(){
        super(HttpStatus.BAD_REQUEST, "LoginError: Password is wrong.");
    }
}
