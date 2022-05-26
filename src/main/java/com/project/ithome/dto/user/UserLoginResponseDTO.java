package com.project.ithome.dto.user;

import com.project.ithome.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户响应类")
public class UserLoginResponseDTO implements Serializable {
    @ApiModelProperty("账号id")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("角色")
    private Role role;
    @ApiModelProperty("响应信息")
    private String msg;
    @ApiModelProperty("token")
    private String token;
}
