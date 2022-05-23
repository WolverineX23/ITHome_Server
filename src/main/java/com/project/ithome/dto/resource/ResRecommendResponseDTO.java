package com.project.ithome.dto.resource;

import com.project.ithome.entity.ResInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResRecommendResponseDTO implements Serializable {
    private ResInfo sameRes;
    private String recommender;
    private RecommendResult result;
    private String msg;
}
