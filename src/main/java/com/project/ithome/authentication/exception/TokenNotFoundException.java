package com.project.ithome.authentication.exception;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends BaseException {
    private static final long serialVersionUID = 5388121573752286530L;
    public TokenNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Token is not found, please login again.");
    }
}
