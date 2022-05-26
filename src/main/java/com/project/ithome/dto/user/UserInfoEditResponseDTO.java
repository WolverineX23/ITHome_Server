package com.project.ithome.dto.user;

import com.project.ithome.entity.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("编辑用户信息响应类")
public class UserInfoEditResponseDTO implements Serializable {
    @ApiModelProperty("用户对象")
    private UserInfo user;
    @ApiModelProperty("响应信息")
    private String msg;
}
