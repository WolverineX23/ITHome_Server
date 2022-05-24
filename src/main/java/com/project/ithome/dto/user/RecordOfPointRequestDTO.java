package com.project.ithome.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordOfPointRequestDTO implements Serializable {
    private int pageNum;
    private int pageSize;
}
