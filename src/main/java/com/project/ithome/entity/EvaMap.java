package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("eva_map")
@Data
@NoArgsConstructor
public class EvaMap implements Serializable {

    @TableId(type = IdType.AUTO)
    private int mapId;

    private String userId;
    private String resId;
    private String evaId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime timeModified;

    public EvaMap(String userId, String resId, String evaId) {
        this.userId = userId;
        this.resId = resId;
        this.evaId = evaId;
    }
}
