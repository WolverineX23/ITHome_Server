package com.project.ithome.exception.user;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NoResultSearchException extends BaseException {
    private static final long serialVersionUID = -8231664887429288798L;

    public NoResultSearchException(String content) {
        super(HttpStatus.NOT_FOUND, "search error: no user with the content:" + content);
    }
}
