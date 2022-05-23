package com.project.ithome.dto.resource;

import com.project.ithome.entity.ResInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassedResInfoDTO implements Serializable {
    private ResInfo resource;
    private String recommenderId;
    private String recommenderName;
    private LocalDateTime passedTime;
    private String msg;
}
