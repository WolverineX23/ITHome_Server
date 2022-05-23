package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "user_info", autoResultMap = true)
@NoArgsConstructor
public class UserInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    private String userId;
    private String userName;
    private String password;
    private int point;
    private int level;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> interestTag;   //兴趣标签  数组
    private Role role;
    private String email;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timeCreated;
    @TableField(fill =FieldFill.INSERT_UPDATE)
    private LocalDateTime timeModified;

    public UserInfo(String userId, String userName, String password, int point, int level, List<String> interestTag, Role role, String email) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.point = point;
        this.level = level;
        this.interestTag = interestTag;
        this.role = role;
        this.email = email;
    }
}
