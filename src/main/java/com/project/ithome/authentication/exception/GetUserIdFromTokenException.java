package com.project.ithome.authentication.exception;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class GetUserIdFromTokenException extends BaseException {
    private static final long serialVersionUID = 2307417901061864958L;
    public GetUserIdFromTokenException() {
        super(HttpStatus.CONFLICT, "JWTDecodeException: get userId from token.");
    }
}
