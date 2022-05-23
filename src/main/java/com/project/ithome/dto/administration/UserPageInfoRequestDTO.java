package com.project.ithome.dto.administration;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageInfoRequestDTO implements Serializable {
    private int pageNum;
    private int pageSize;
}
