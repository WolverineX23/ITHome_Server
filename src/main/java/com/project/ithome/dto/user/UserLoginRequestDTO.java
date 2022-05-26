package com.project.ithome.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户登录请求类")
public class UserLoginRequestDTO implements Serializable {
    @ApiModelProperty("账号id")
    private String UserId;
    @ApiModelProperty("密码")
    private String password;
}
