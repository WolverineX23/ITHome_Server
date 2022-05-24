package com.project.ithome.dto.resource;

import com.project.ithome.entity.ResInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassedResInfoResponseDTO implements Serializable {
    private ResInfo resource;
    private String recommenderId;
    private String recommenderName;
    private LocalDateTime passedTime;
    private List<EvaluationDTO> evaluationList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
