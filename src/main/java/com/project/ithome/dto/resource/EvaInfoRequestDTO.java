package com.project.ithome.dto.resource;

import lombok.Data;

import java.io.Serializable;

@Data
public class EvaInfoRequestDTO implements Serializable {
    private int star;
    private String comment;
}
