package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@TableName(value = "res_info", autoResultMap = true)
@Data
@NoArgsConstructor
public class ResInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    private String resId;
    private String resName;
    private String resLink;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> techTag;
    private int colAmount;
    private int views;
    private Status status;
    private String resDescription;
    private String resDetail;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime timeModified;

    public ResInfo(String resId, String resName, String resLink, List<String> techTag, int colAmount, int views, Status status, String resDescription, String resDetail) {
        this.resId = resId;
        this.resName = resName;
        this.resLink = resLink;
        this.techTag = techTag;
        this.colAmount = colAmount;
        this.views = views;
        this.status = status;
        this.resDescription = resDescription;
        this.resDetail = resDetail;
    }
}
