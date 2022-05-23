package com.project.ithome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("opera_info")
@Data
@NoArgsConstructor
public class OperaInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private int operaId;
    private String operaName;
    private int score;

}
