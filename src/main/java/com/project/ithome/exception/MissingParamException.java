package com.project.ithome.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class MissingParamException extends BaseException{
    private static final long serialVersionUID = 8399518610048405928L;

    public MissingParamException(String[] param){
        super(HttpStatus.BAD_REQUEST, "Missing Param: " + Arrays.toString(param));
    }
}
