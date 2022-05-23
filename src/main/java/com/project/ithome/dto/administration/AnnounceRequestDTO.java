package com.project.ithome.dto.administration;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnnounceRequestDTO implements Serializable {
    private String content;
    //private String title;
}
