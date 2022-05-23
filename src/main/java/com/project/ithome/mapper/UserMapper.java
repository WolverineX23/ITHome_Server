package com.project.ithome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.ithome.entity.UserInfo;
import org.springframework.stereotype.Repository;

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
}
