package com.project.ithome.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendListResponseDTO implements Serializable {
    private List<FriendInfoDTO> friendList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
