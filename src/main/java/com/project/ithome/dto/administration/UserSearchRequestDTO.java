package com.project.ithome.dto.administration;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchRequestDTO implements Serializable {
    private int pageNum;
    private int pageSize;
}
