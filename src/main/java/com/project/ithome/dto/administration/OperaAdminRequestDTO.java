package com.project.ithome.dto.administration;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperaAdminRequestDTO implements Serializable {
    private String userId;
    private int adminOpera; //0为设立，1为废除
}
