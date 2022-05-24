package com.project.ithome.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankInfo implements Serializable {
    private String userId;
    private String userName;
    private int point;
    private int rank;
}
