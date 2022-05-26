package com.project.ithome.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("")
public class PointRecord implements Serializable {
    private String recordDesc;
    private int point;
    private LocalDateTime recTime;
}
