package com.project.ithome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class UserInfoGetRequestDTO implements Serializable {
    private String userId;
}
