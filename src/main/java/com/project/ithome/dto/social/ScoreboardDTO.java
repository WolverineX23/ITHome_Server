package com.project.ithome.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreboardDTO implements Serializable {
    private List<RankInfo> scoreBoard;              //总积分       也可附加周积分榜和月积分榜等，sql实现
    private String msg;
}
