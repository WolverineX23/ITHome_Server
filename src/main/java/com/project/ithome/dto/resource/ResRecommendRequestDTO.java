package com.project.ithome.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResRecommendRequestDTO implements Serializable {
    private String resName;
    private String resDescription;
    private String resLink;
    private List<String> techTag;
    private String resDetail;
}
