package com.project.ithome.dto.social;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnnounceListRequestDTO implements Serializable {
    private int pageNum;
    private int pageSize;
}
