package com.project.ithome.exception.resource;

import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    private static final long serialVersionUID = 7517312472827573533L;
    public ResourceNotFoundException(String resId) {
        super(HttpStatus.NOT_FOUND, "Resource:" + resId + "haven't found.");
    }
}
