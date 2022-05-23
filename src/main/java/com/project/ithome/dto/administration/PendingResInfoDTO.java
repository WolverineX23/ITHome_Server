package com.project.ithome.dto.administration;

import com.project.ithome.entity.ResInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingResInfoDTO implements Serializable {
    private String resName;
    private String resDescription;
    private String resLink;
    private List<String> techTag;
    private String resDetail;
    private String recommenderId;
    private String recommenderName;
    private String msg;
}
