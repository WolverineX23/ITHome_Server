package com.project.ithome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dateTime;
    private String message;
    private String debugMessage;

    public RestError(){
        this.dateTime = LocalDateTime.now();
    }

    public RestError(Throwable e){
        this();
        this.message = e.getMessage();
        this.debugMessage = e.getLocalizedMessage();
    }

    public RestError(Throwable e, String msg){
        this();
        this.message=msg;
        this.debugMessage = e.getLocalizedMessage();
    }
}
