package com.project.ithome.dto.resource;

import lombok.Data;

import java.io.Serializable;

@Data
public class PassedResInfoRequestDTO implements Serializable {
    private int pageNum;        //评论区的页码
    private int pageSize;       //评论区的页大小
}
