package com.project.ithome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordOfPointResponseDTO implements Serializable {
    private List<PointRecord> recordList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
