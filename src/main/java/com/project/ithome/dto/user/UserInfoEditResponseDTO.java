package com.project.ithome.dto.user;

import com.project.ithome.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEditResponseDTO implements Serializable {
    private UserInfo user;
    private String msg;
}
