package com.project.ithome.authentication.exception;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    private static final long serialVersionUID = -6072795630224705321L;
    public UserNotFoundException(String userId) {
        super(HttpStatus.NOT_FOUND, "user:" + userId + " is not found");
    }
}
