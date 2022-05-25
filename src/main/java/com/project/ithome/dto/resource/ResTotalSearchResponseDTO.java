package com.project.ithome.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResTotalSearchResponseDTO implements Serializable {
    private List<ResourceResume> resResumeList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
