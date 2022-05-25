package com.project.ithome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.ithome.dto.social.RankInfo;
import com.project.ithome.entity.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<UserInfo> {
    //到这一步已经把简单的CRUD的编写完成了，不在是像之前mybatis一样编写接口和XXXMapper.xml，简化开发！

    /*
    //注册用户
    public void registerUser(UserInfo user);

    //用户信息编辑
    public void editUserInfo(String userName);

    //获取某位用户信息
    public UserInfo queryUserInfo(String userName);
    */

    //获取某人的积分排名(同积分同排名模式--Rank)
    int getRankByUserId(String userId);

    //获取某人的积分排名(从1开始应用的每一行分配一个序号--Row_Number)
    int getRowNumberByUserId(String userId);

    //总积分榜（前十位）
    List<RankInfo> getTotalScoreboard();

    //根据Id获得name
    String getUserNameById(String userId);
}
