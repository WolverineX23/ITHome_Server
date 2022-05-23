package com.project.ithome.dto.user;

import com.project.ithome.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoGetResponseDTO implements Serializable {
    private UserInfo user;
    private String msg;
}
