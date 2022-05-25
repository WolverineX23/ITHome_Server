package com.project.ithome.dto.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResColSearchRequestDTO implements Serializable {
    private List<String> tagArray;
    private OrderAttr orderAttr;
    private int pageNum;
    private int pageSize;
}
