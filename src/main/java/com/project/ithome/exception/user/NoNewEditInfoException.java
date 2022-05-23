package com.project.ithome.exception.user;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NoNewEditInfoException extends BaseException {
    private static final long serialVersionUID = 5195663577126616780L;
    public NoNewEditInfoException() {
        super(HttpStatus.BAD_REQUEST, "No new editInfo exception.");
    }
}
