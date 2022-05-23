package com.project.ithome.dto.administration;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamineResRequestDTO implements Serializable {
    private String recommenderId;
    private ExamineResult result;
    private String reasonDesc;
}
