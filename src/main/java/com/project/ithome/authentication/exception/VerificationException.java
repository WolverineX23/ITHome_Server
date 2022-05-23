package com.project.ithome.authentication.exception;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class VerificationException extends BaseException {
    private static final long serialVersionUID = -2308038535699903377L;
    public VerificationException() {
        super(HttpStatus.CONFLICT, "JWT verification exception");
    }
}
