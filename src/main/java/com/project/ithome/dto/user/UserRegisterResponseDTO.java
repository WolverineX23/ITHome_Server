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
@ApiModel("用户注册响应类")
public class UserRegisterResponseDTO implements Serializable {
    @ApiModelProperty("账号id")
    private String userId;
    @ApiModelProperty("响应信息")
    private String msg;
}
