package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@TableName(value = "friend_info", autoResultMap = true)
@Data
@NoArgsConstructor
public class FriendInfo implements Serializable {
    @TableId(type = IdType.INPUT)
    private String friendId;

    private String userId;
    private String profile;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tag;
    private String communication;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime timeModified;

    public FriendInfo(String friendId, String userId, String profile, List<String> tagList, String communication) {
        this.friendId = friendId;
        this.userId = userId;
        this.profile = profile;
        this.tag = tagList;
        this.communication = communication;
    }
}
