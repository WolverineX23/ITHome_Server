package com.project.ithome.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendInfoFillResponseDTO implements Serializable {
    private String msg;
}
