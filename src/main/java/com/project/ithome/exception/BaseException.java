package com.project.ithome.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends Exception{
    private static final long serialVersionUID = 7014340272379942672L;

    private HttpStatus status;

    public BaseException(HttpStatus status, String msg){
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
