package com.project.ithome.dto.administration;

import com.project.ithome.dto.resource.ResourceResume;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingResPageResponseDTO implements Serializable {
    private List<ResourceResume> resResumeList;     //无需colAmount;
    private int pageCount;
    private int totalCount;
    private String msg;
}
