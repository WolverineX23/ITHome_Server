package com.project.ithome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAnnounceResponseDTO implements Serializable {
    private List<AnnounceInfo> announceList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
