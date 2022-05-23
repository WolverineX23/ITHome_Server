package com.project.ithome.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceResponseDTO implements Serializable {
    private String msg;
}
