package com.project.ithome.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageInfoResponseDTO implements Serializable {
    private List<UserPageInfo> adminPage;
    private int adminPageCount;
    private int adminTotalCount;
    private List<UserPageInfo> normalUserPage;
    private int normalUserPageCount;
    private int normalUserTotalCount;
    private String msg;
}
