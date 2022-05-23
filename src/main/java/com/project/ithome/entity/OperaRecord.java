package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("opera_record")
@Data
@NoArgsConstructor
public class OperaRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private int recId;
    private String userId;
    private int operaId;
    private String resId;
    private String recDescription;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime timeModified;

    public OperaRecord(String userId, int operaId, String recDescription){      //每日登录——无resId （数据插入默认为0）
        this.userId = userId;
        this.operaId = operaId;
        this.recDescription = recDescription;
    }

    public OperaRecord(String userId, int operaId, String resId, String recDescription) {
        this.userId = userId;
        this.operaId = operaId;
        this.resId = resId;
        this.recDescription = recDescription;
    }
}
