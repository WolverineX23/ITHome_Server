package com.project.ithome.dto.user;

import com.project.ithome.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO implements Serializable {
    private String userId;
    private String userName;
    private Role role;
    private String msg;
    private String token;
}
