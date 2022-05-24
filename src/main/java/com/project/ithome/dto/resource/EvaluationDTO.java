package com.project.ithome.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO implements Serializable {
    private String evaluatorId;
    private String evaluatorName;
    private int star;
    private String comment;
    private LocalDateTime evaTime;
}
