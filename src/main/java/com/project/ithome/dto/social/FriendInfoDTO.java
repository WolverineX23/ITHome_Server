package com.project.ithome.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendInfoDTO implements Serializable {
    private String userId;
    private String userName;
    private String profile;
    private List<String> tagList;
    private String communication;
    private LocalDateTime timeReleased;
}
