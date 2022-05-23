package com.project.ithome.dto.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResume implements Serializable {
    private String resId;
    private String resName;
    private List<String> techTag;
    private String resDescription;
    private int colAmount;
    private String resLink;
}
