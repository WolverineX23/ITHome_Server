package com.project.ithome.exception.resource;

import com.project.ithome.entity.ResInfo;
import com.project.ithome.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceIsExistedException extends BaseException {
    private static final long serialVersionUID = 2760893064921257115L;

    public ResourceIsExistedException(ResInfo resource) {
        super(HttpStatus.BAD_REQUEST, "RecommendError: A resource similar to the recommended resource already exists. resourceInfo:" + resource );
    }
}
