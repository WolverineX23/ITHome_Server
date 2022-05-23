package com.project.ithome.dto.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResSearchRequestDTO implements Serializable {
    private List<String> tagArray;
    private int pageNum;
    private int pageSize;
}
