package com.project.ithome.dto.social;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FriendInfoFillRequestDTO implements Serializable {
    private String profile;
    private List<String> tagList;
    private String communication;
}
