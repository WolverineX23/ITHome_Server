package com.project.ithome.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResGetByTechTagRequestDTO implements Serializable {
    private List<String> techTag;
    private int pageNum;
    private int pageSize;
}
