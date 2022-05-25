package com.project.ithome.service.social;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.ithome.dto.social.*;
import com.project.ithome.entity.FriendInfo;
import com.project.ithome.entity.OperaRecord;
import com.project.ithome.entity.UserInfo;
import com.project.ithome.mapper.FriendMapper;
import com.project.ithome.mapper.RecordMapper;
import com.project.ithome.mapper.UserMapper;
import com.project.ithome.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, FriendInfo> implements FriendService {
    private final Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);
    private final FriendMapper friendMapper;
    private final RecordMapper recordMapper;
    private final UserMapper userMapper;
    public FriendServiceImpl(FriendMapper friendMapper, RecordMapper recordMapper, UserMapper userMapper) {
        this.friendMapper = friendMapper;
        this.recordMapper = recordMapper;
        this.userMapper = userMapper;
    }


    @Override
    public List<FriendInfo> queryFriendInfoInPage(QueryWrapper<FriendInfo> wrapper, int pageNum, int pageSize) {
        IPage<FriendInfo> page = new Page<>(pageNum, pageSize);
        page = friendMapper.selectPage(page, wrapper);
        List<FriendInfo> friendList = page.getRecords();
        logger.info("friend list: {}", friendList);
        return friendList;
    }

    @Override
    public List<FriendInfoDTO> parseFriendInfoDTO(List<FriendInfo> friendInfoList) {
        List<FriendInfoDTO> friendList = new ArrayList<>();
        for(FriendInfo friendInfo : friendInfoList) {
            String userName = userMapper.getUserNameById(friendInfo.getUserId());
            FriendInfoDTO friend = new FriendInfoDTO(friendInfo.getUserId(), userName, friendInfo.getProfile(), friendInfo.getTag(), friendInfo.getCommunication(), friendInfo.getTimeCreated());
            friendList.add(friend);
        }
        return friendList;
    }

    @Override
    public FriendInfoFillResponseDTO fillFriendInfo(FriendInfoFillRequestDTO req, String userId) {
        String friendId = UuidUtil.getUuid();
        logger.info("friend uuid:{}",friendId);
        FriendInfo friendInfo = new FriendInfo(friendId, userId, req.getProfile(), req.getTagList(), req.getCommunication());
        int friendInsert = friendMapper.insert(friendInfo);
        logger.info("Insert {} data in friend_info table. info:{}", friendInsert, friendInfo);
        //判断是否为第一次发布个人介绍
        QueryWrapper<OperaRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("opera_id", 4);
        List<OperaRecord> recordList = recordMapper.selectList(wrapper);
        if(recordList.isEmpty()) {      //第一次发布
            OperaRecord record = new OperaRecord(userId, 4, "找伙伴：首次发布自我介绍");
            int recInsert = recordMapper.insert(record);
            logger.info("first fill friend info. Insert {} record int opera_record table", recInsert);

            UserInfo user = userMapper.selectById(userId);
            user.setPoint(user.getPoint() + 5);
            if(user.getPoint() / 30 != user.getLevel())
                user.setLevel(user.getLevel() + 1);
            int userUpdate = userMapper.updateById(user);
            logger.info("Update {} date about point and level in user table.", userUpdate);
        }

        return new FriendInfoFillResponseDTO("success");
    }

    @Override
    public FriendListResponseDTO getFriendList(FriendListRequestDTO req) {
        QueryWrapper<FriendInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("time_created");
        List<FriendInfo> friendInfoList = queryFriendInfoInPage(wrapper, req.getPageNum(), req.getPageSize());
        List<FriendInfoDTO> friendList = parseFriendInfoDTO(friendInfoList);
        logger.info("friendList dto:{}",friendList);
        int pageCount = friendList.size();
        int totalCount = friendMapper.selectCount(wrapper).intValue();
        logger.info("pageCount:{}, totalCount:{}", pageCount, totalCount);

        return new FriendListResponseDTO(friendList, pageCount, totalCount, "success");
    }
}
