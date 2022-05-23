package com.project.ithome.exception.resource;

import com.project.ithome.entity.Role;
import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UltraViresException extends BaseException {
    private static final long serialVersionUID = -4040994554515018057L;
    public UltraViresException(String userId) {
        super(HttpStatus.BAD_REQUEST, "user(" + userId +") have no access to the api.");
    }
}
