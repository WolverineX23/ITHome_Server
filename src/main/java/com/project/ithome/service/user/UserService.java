package com.project.ithome.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.ithome.authentication.exception.UserNotFoundException;
import com.project.ithome.dto.administration.*;
import com.project.ithome.dto.social.*;
import com.project.ithome.dto.user.*;
import com.project.ithome.entity.OperaRecord;
import com.project.ithome.entity.UserInfo;
import com.project.ithome.exception.resource.UltraViresException;
import com.project.ithome.exception.user.NoNewEditInfoException;
import com.project.ithome.exception.user.NoResultSearchException;
import com.project.ithome.exception.user.RegisterException;
import com.project.ithome.exception.user.WrongPasswordException;

import java.util.List;

public interface UserService extends IService<UserInfo> {

    //判断账号是否存在
    boolean isUserIdExisted(String userId);

    //账号注册
    UserRegisterResponseDTO userRegister(UserRegisterRequestDTO registerInfo)
        throws RegisterException;

    //账号登录(账号注册后，依据条件timeCreated == timeModified,注册当天第一次登录仍能加积分)
    UserLoginResponseDTO userLogin(UserLoginRequestDTO loginInfo)
        throws WrongPasswordException, UserNotFoundException;

    //个人信息获取
    UserInfoGetResponseDTO getUserInfo(UserInfoGetRequestDTO getInfo);

    //个人信息编辑(与原始数据相同的情况，返回异常提醒)
    UserInfoEditResponseDTO editUserInfo(UserInfoEditRequestDTO editInfo) throws NoNewEditInfoException;

    //判断用户是否越权(StationMaster权限）
    boolean isStationMaster(String userId);

    //判断用户是否越权(admin及以上权限)
    boolean isNormalUser(String userId);

    //将UserInfo 转化为 UserPageInfo
    List<UserPageInfo> parseUserPageInfo(List<UserInfo> userInfoList);

    //将UserInfo 转化为 UserSearchResult
    List<UserSearchResult> parseUserSearchResult(List<UserInfo> userInfoList);

    //将UserInfo 转化为 SelfRankInfo
    List<RankInfo> parseUserRankInfo(List<UserInfo> userInfoList);

    //用户分页查询
    List<UserInfo> queryUserInPage(QueryWrapper<UserInfo> wrapper, int pageNum, int pageSize);

    //获取管理员列表和用户列表  站长权限
    UserPageInfoResponseDTO getUserPageInfo(UserPageInfoRequestDTO req, String masterId) throws UltraViresException;

    //搜索用户(其中 未搜索到用户的异常抛出非必要)   站长权限
    UserSearchResponseDTO searchUser(UserSearchRequestDTO searchInfo, String masterId, String content) throws UltraViresException, NoResultSearchException;

    //设立/废除管理员  站长权限
    OperaAdminResponseDTO operaAdmin(OperaAdminRequestDTO operaInfo, String masterId) throws UltraViresException;

    //公告分页查询
    List<OperaRecord> queryAnnounceRecordInPage(QueryWrapper<OperaRecord> wrapper, int pageNum, int pageSize);

    //将 OperaRecord 转化为 AnnounceInfo
    List<AnnounceInfo> parseAnnounceInfo(List<OperaRecord> recordList);

    //获取历史公告 无权限限制
    AnnounceListResponseDTO getAnnounceList(AnnounceListRequestDTO req);

    //发布公告
    AnnounceResponseDTO announce(AnnounceRequestDTO req, String masterId) throws UltraViresException;

    //获取积分榜
    ScoreboardDTO getScoreboard();

    //获取个人排名
    SelfRankDTO getSelfRank(String userId);
}
