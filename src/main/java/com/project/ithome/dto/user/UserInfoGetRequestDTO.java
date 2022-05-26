package com.project.ithome.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("获取用户信息请求类")
public class UserInfoGetRequestDTO implements Serializable {
    @ApiModelProperty("账号id")
    private String userId;
}
