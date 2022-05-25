package com.project.ithome.service.social;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.ithome.dto.social.*;
import com.project.ithome.entity.FriendInfo;

import java.util.List;

public interface FriendService extends IService<FriendInfo> {

    //分页查找朋友信息列表
    List<FriendInfo> queryFriendInfoInPage(QueryWrapper<FriendInfo> wrapper, int pageNum, int pageSize);

    //将FriendInfo 转化为 FriendInfoDTO
    List<FriendInfoDTO> parseFriendInfoDTO(List<FriendInfo> friendInfoList);

    //找朋友：发布个人信息
    FriendInfoFillResponseDTO fillFriendInfo(FriendInfoFillRequestDTO req, String userId);

    //找朋友：获取信息列表
    FriendListResponseDTO getFriendList(int pageNum, int pageSize);
}
