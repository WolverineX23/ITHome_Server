package com.project.ithome.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("积分记录类")
public class RecordOfPointResponseDTO implements Serializable {
    @ApiModelProperty("积分记录列表")
    private List<PointRecord> recordList;
    @ApiModelProperty("当页记录条数")
    private int pageCount;
    @ApiModelProperty("总记录条数")
    private int totalCount;
    @ApiModelProperty("响应信息")
    private String msg;
}
