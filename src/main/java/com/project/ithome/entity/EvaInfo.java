package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("eva_info")
@Data
@NoArgsConstructor
public class EvaInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    private String evaId;   //14位Id

    private int star;       //星级：1-5级
    private String comment;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime timeModified;

    public EvaInfo(String evaId, int star, String comment) {
        this.evaId = evaId;
        this.star = star;
        this.comment = comment;
    }
}
