package com.project.ithome.dto.administration;

import lombok.Data;

import java.io.Serializable;

@Data
public class PendingResPageRequestDTO implements Serializable {
    private int pageNum;
    private int pageSize;
}
