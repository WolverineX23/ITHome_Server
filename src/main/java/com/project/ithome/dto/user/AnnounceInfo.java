package com.project.ithome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnounceInfo implements Serializable {
    private String announcerId;
    private String announceDesc;
    private LocalDateTime timeCreated;
}
