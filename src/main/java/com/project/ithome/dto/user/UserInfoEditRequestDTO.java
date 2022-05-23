package com.project.ithome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEditRequestDTO implements Serializable {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private List<String> interestTag;
}
