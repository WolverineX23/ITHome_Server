package com.project.ithome.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponseDTO implements Serializable {
    private List<UserSearchResult> userSearchResultList;
    private int pageCount;
    private int totalCount;
    private String msg;
}
