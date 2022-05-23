package com.project.ithome.dto.administration;

import com.project.ithome.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageInfo implements Serializable {
    private String userId;
    private String userName;
    private int point;
}
