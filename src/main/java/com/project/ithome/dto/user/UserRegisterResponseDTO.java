package com.project.ithome.dto.user;

import com.project.ithome.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponseDTO implements Serializable {
    private String userId;
    private String msg;
}
