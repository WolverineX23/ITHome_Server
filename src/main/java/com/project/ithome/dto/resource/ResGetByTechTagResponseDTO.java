package com.project.ithome.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResGetByTechTagResponseDTO implements Serializable {
    private List<ResourceResume> resResumeList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
