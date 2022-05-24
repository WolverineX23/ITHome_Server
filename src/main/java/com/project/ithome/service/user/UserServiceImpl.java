package com.project.ithome.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.ithome.authentication.exception.UserNotFoundException;
import com.project.ithome.authentication.service.TokenService;
import com.project.ithome.dto.administration.*;
import com.project.ithome.dto.social.*;
import com.project.ithome.dto.user.*;
import com.project.ithome.entity.OperaInfo;
import com.project.ithome.entity.OperaRecord;
import com.project.ithome.entity.Role;
import com.project.ithome.entity.UserInfo;
import com.project.ithome.exception.resource.UltraViresException;
import com.project.ithome.exception.user.NoNewEditInfoException;
import com.project.ithome.exception.user.NoResultSearchException;
import com.project.ithome.exception.user.RegisterException;
import com.project.ithome.exception.user.WrongPasswordException;
import com.project.ithome.mapper.OperationMapper;
import com.project.ithome.mapper.RecordMapper;
import com.project.ithome.mapper.UserMapper;
import com.project.ithome.util.RandomAccountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserInfo>  implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final RecordMapper recordMapper;
    private final OperationMapper operationMapper;

    public UserServiceImpl(UserMapper userMapper, RecordMapper recordMapper, OperationMapper operationMapper) {
        this.userMapper = userMapper;
        this.recordMapper = recordMapper;
        this.operationMapper = operationMapper;
    }


    @Override
    public boolean isUserIdExisted(String userId) {
        UserInfo user = userMapper.selectById(userId);
        if(user == null) {
            logger.info("user:{} is nonexistent", userId);
            return false;
        }
        logger.info("user:{} is existed", userId);
        return true;
    }



    @Override
    public UserRegisterResponseDTO userRegister(UserRegisterRequestDTO registerInfo) throws RegisterException {
        boolean isExisted = true;
        String newId = "";
        while(isExisted){                      //生成唯一的userId
            newId = RandomAccountUtil.randomDigitNumber(9);
            isExisted = isUserIdExisted(newId);
        }
        UserInfo user = new UserInfo(newId, registerInfo.getUserName(), registerInfo.getPassword(), 0, 0, new ArrayList<>(), Role.NormalUser, "");
        int insert = userMapper.insert(user);
        logger.info("Insert {} data about register. userId:{}", insert, newId);
        if(insert == 0) {
            throw new RegisterException();
        }
        return new UserRegisterResponseDTO(newId, "success");
    }

    @Override
    public UserLoginResponseDTO userLogin(UserLoginRequestDTO loginInfo) throws WrongPasswordException, UserNotFoundException {
        String userId = loginInfo.getUserId();
        UserInfo user = userMapper.selectById(userId);
        if(user == null)                                                // user isn't existed.
            throw new UserNotFoundException(userId);
        else {
            if(user.getPassword().equals(loginInfo.getPassword())){     // login successfully
                //判断是否为当天首次登录
                int year = LocalDate.now().getYear();
                int day = LocalDate.now().getDayOfYear();  //返回该year的第几天
                if(year != user.getTimeModified().getYear() || day != user.getTimeModified().getDayOfYear() || user.getTimeCreated().equals(user.getTimeModified())) {  //每日首次登录
                    user.setPoint(user.getPoint() + 2);
                    if(user.getPoint() / 30 != user.getLevel())   //每30积分一级，从0级开始。
                        user.setLevel(user.getLevel() + 1);

                    int update = userMapper.updateById(user);
                    logger.info("user:{} first login per day. Update {} Data successfully.", userId, update);

                    OperaRecord operaRecord = new OperaRecord(userId, 1, "每日首次登录");
                    int insert = recordMapper.insert(operaRecord);
                    logger.info("Insert {} record about login successfully.", insert);
                }

                TokenService tokenService = new TokenService();
                String token = tokenService.getToken(user);
                logger.info("Login Token generated: {}", token);
                return new UserLoginResponseDTO(user.getUserId(), user.getUserName(), user.getRole(), "success", token);
            }
            else
                throw new WrongPasswordException();
        }
    }

    @Override
    public UserInfoGetResponseDTO getUserInfo(UserInfoGetRequestDTO getInfo) {
        String userId = getInfo.getUserId();
        UserInfo user = userMapper.selectById(userId);
        UserInfoGetResponseDTO userInfoGetResponseDTO = new UserInfoGetResponseDTO(user, "success");
        logger.info("Get user info {}", user);
        return userInfoGetResponseDTO;
    }

    @Override
    public UserInfoEditResponseDTO editUserInfo(UserInfoEditRequestDTO editInfo) throws NoNewEditInfoException {
        String userId = editInfo.getUserId();
        UserInfo user = userMapper.selectById(userId);
        boolean isEdited = false;

        if(!user.getUserName().equals(editInfo.getUserName())){
            user.setUserName(editInfo.getUserName());
            isEdited = true;
        }
        if(!user.getPassword().equals(editInfo.getPassword())){
            user.setPassword(editInfo.getPassword());
            isEdited = true;
        }
        if(!user.getEmail().equals(editInfo.getEmail())){
            user.setEmail(editInfo.getEmail());
            isEdited = true;
        }
        List<String> userTagList = user.getInterestTag();
        List<String> editTagList = editInfo.getInterestTag();
        logger.info("editInfo.interests:{}", editTagList);
        logger.info("userId:{} have interests about {}", userId, userTagList);
        if(userTagList.size() != editTagList.size() || !userTagList.containsAll(editTagList)) {
            user.setInterestTag(editTagList);
            isEdited = true;
        }
        if(!isEdited)
            throw new NoNewEditInfoException();

        //条件查询          MP条件构造器wrapper
        QueryWrapper<OperaRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("opera_id", 6);
        List<OperaRecord> record = recordMapper.selectList(wrapper);
        logger.info("user:{} first edit userInfo record--{}", userId, record);
        /*
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("opera_id", 6);
        List<OperaRecord> record = recordMapper.selectByMap(map);
        */

        //这里record为[] 不等于 record == null
        if(record.isEmpty()) {
            user.setPoint(user.getPoint() + 5);
            if(user.getPoint() / 30 != user.getLevel())
                user.setLevel(user.getLevel() + 1);
            OperaRecord operaRecord = new OperaRecord(userId, 6, "首次编辑个人资料");
            int insert = recordMapper.insert(operaRecord);
            logger.info("Insert {} record about edit userInfo successfully.", insert);
        }
        int update = userMapper.updateById(user);
        logger.info("user:{} first edit userInfo. Update {} Data successfully.", userId, update);

        UserInfoEditResponseDTO userInfoEditResponseDTO = new UserInfoEditResponseDTO(user, "success");
        logger.info("userInfo after editing:{}", user);

        return userInfoEditResponseDTO;
    }

    @Override
    public boolean isStationMaster(String userId) {
        UserInfo user = userMapper.selectById(userId);
        Role role = user.getRole();
        if(role.equals(Role.StationMaster)) {
            logger.info("User{} with role of {}  have access to the api.", userId, role);
            return true;
        }
        logger.info("User{} with role of {}  have no access to the api.", userId, role);
        return false;
    }

    @Override
    public boolean isNormalUser(String userId) {
        UserInfo user = userMapper.selectById(userId);
        Role role = user.getRole();
        if(role.equals(Role.NormalUser)){
            logger.info("User{} with role of {}  have no access to the api.", userId, role);
            return true;
        }
        logger.info("User{} with role of {}  have access to the api.", userId, role);
        return false;
    }

    @Override
    public List<UserPageInfo> parseUserPageInfo(List<UserInfo> userInfoList) {
        List<UserPageInfo> userPageInfoList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            UserPageInfo userPageInfo = new UserPageInfo();
            userPageInfo.setUserId(userInfo.getUserId());
            userPageInfo.setUserName(userInfo.getUserName());
            userPageInfo.setPoint(userInfo.getPoint());
            userPageInfoList.add(userPageInfo);
        }
        return userPageInfoList;
    }

    @Override
    public List<UserSearchResult> parseUserSearchResult(List<UserInfo> userInfoList) {
        List<UserSearchResult> userSearchResultList = new ArrayList<>();
        for(UserInfo userInfo : userInfoList) {
            UserSearchResult userSearchResult = new UserSearchResult(userInfo.getUserId(), userInfo.getUserName(), userInfo.getPoint(), userInfo.getRole());
            userSearchResultList.add(userSearchResult);
        }
        return userSearchResultList;
    }

    @Override
    public List<RankInfo> parseUserRankInfo(List<UserInfo> userInfoList) {
        List<RankInfo> rankInfoList = new ArrayList<>();
        for(int i = 0; i < userInfoList.size(); ++i) {
            UserInfo user = userInfoList.get(i);
            RankInfo rankInfo = new RankInfo(user.getUserId(), user.getUserName(), user.getPoint(), i+1);
            rankInfoList.add(rankInfo);
        }
        logger.info("selfRankInfoList:{}", rankInfoList);
        return rankInfoList;
    }

    @Override
    public List<UserInfo> queryUserInPage(QueryWrapper<UserInfo> wrapper, int pageNum, int pageSize) {
        IPage<UserInfo> page = new Page<>(pageNum, pageSize);
        page = userMapper.selectPage(page, wrapper);
        return page.getRecords();
    }

    @Override
    public UserPageInfoResponseDTO getUserPageInfo(UserPageInfoRequestDTO req, String masterId) throws UltraViresException {
        if(!isStationMaster(masterId))
            throw new UltraViresException(masterId);
        //获取管理员信息列表
        QueryWrapper<UserInfo> adminWrapper = new QueryWrapper<>();
        adminWrapper.eq("role", Role.Admin).orderByDesc("point");       //根据积分排名
        List<UserInfo> adminInfoList = queryUserInPage(adminWrapper, req.getPageNum(), req.getPageSize());
        List<UserPageInfo> adminPageInfoList = parseUserPageInfo(adminInfoList);  //数据压缩（page中每个数据仅有id和name）
        int adminPageCount = adminPageInfoList.size();      //管理员页表本页人数
        int adminTotalCount = userMapper.selectCount(adminWrapper).intValue();
        logger.info("admin pageCount:{}, totalCount:{}, admin page list: {}", adminPageCount, adminTotalCount, adminPageInfoList);
        //获取普通用户信息列表
        QueryWrapper<UserInfo> normalUserWrapper = new QueryWrapper<>();
        normalUserWrapper.eq("role", Role.NormalUser).orderByDesc("point");
        List<UserInfo> normalUserinfoList = queryUserInPage(normalUserWrapper, req.getPageNum(), req.getPageSize());
        List<UserPageInfo> normalUserPageInfoList = parseUserPageInfo(normalUserinfoList);
        int normalUserPageCount = normalUserPageInfoList.size();
        int normalUserTotalCount = userMapper.selectCount(normalUserWrapper).intValue();
        logger.info("normalUser pageCount:{}, totalCount:{}, normalUser page list: {}", normalUserPageCount, normalUserTotalCount, normalUserPageInfoList);

        return new UserPageInfoResponseDTO(adminPageInfoList, adminPageCount, adminTotalCount, normalUserPageInfoList, normalUserPageCount, normalUserTotalCount, "success");
    }

    @Override
    public UserSearchResponseDTO searchUser(UserSearchRequestDTO searchInfo, String masterId, String content) throws UltraViresException, NoResultSearchException {
        if(!isStationMaster(masterId))
            throw new UltraViresException(masterId);
        //搜索
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", content).or().eq("user_name", content);
        int totalCount = userMapper.selectCount(userWrapper).intValue();
        if(totalCount == 0) {
            logger.info("userSearch: no user with the searching content:{}",content);
            throw new NoResultSearchException(content);
        }
        List<UserInfo> userInfoList = queryUserInPage(userWrapper, searchInfo.getPageNum(), searchInfo.getPageSize());
        List<UserSearchResult> userSearchResultList = parseUserSearchResult(userInfoList);
        int pageCount = userSearchResultList.size();
        logger.info("userSearchResult pageCount:{}, totalCount:{}, page list: {}", pageCount, totalCount, userSearchResultList);
        return new UserSearchResponseDTO(userSearchResultList, pageCount, totalCount, "success");
    }

    @Override
    public OperaAdminResponseDTO operaAdmin(OperaAdminRequestDTO operaInfo, String masterId) throws UltraViresException {
        if(!isStationMaster(masterId))
            throw new UltraViresException(masterId);

        UserInfo userInfo = userMapper.selectById(operaInfo.getUserId());
        OperaRecord record = new OperaRecord();
        record.setUserId(operaInfo.getUserId());
        //设立管理员
        if(operaInfo.getAdminOpera() == 0) {
            userInfo.setRole(Role.Admin);
            int update = userMapper.updateById(userInfo);
            logger.info("{} data updated in user_info table. Set up user:{} to be an admin.", update, operaInfo.getUserId());
            record.setOperaId(10);
            record.setRecDescription("被站长" + masterId + "设立为管理员");
        }
        else {  //废除管理员
            userInfo.setRole(Role.NormalUser);
            int update = userMapper.updateById(userInfo);
            logger.info("{} data is updated in user_info table. Remove the admin role of  user:{}.", update, operaInfo.getUserId());
            record.setOperaId(11);
            record.setRecDescription("被站长" + masterId + "废除管理员");
        }
        int insert = recordMapper.insert(record);
        logger.info("{} data is inserted in opera_record table. record:{}", insert, record);

        return new OperaAdminResponseDTO("success");
    }

    @Override
    public List<OperaRecord> queryRecordInPage(QueryWrapper<OperaRecord> wrapper, int pageNum, int pageSize) {
        IPage<OperaRecord> recordPage = new Page<>(pageNum, pageSize);
        recordPage = recordMapper.selectPage(recordPage, wrapper);
        return recordPage.getRecords();
    }

    @Override
    public List<AnnounceInfo> parseAnnounceInfo(List<OperaRecord> recordList) {
        List<AnnounceInfo> announceInfoList = new ArrayList<>();
        for(OperaRecord operaRecord : recordList) {
            AnnounceInfo announceInfo = new AnnounceInfo(operaRecord.getUserId(), operaRecord.getRecDescription(), operaRecord.getTimeCreated());
            announceInfoList.add(announceInfo);
        }
        return announceInfoList;
    }

    @Override
    public List<PointRecord> parsePointRecord(List<OperaRecord> recordList) {
        List<PointRecord> pointRecList = new ArrayList<>();
        for (OperaRecord operaRecord : recordList) {
            PointRecord pointRecord = new PointRecord();
            pointRecord.setRecTime(operaRecord.getTimeCreated());
            int operaId = operaRecord.getOperaId();
            OperaInfo operaInfo = operationMapper.selectById(operaId);
            pointRecord.setPoint(operaInfo.getScore());
            pointRecord.setRecordDesc(operaInfo.getOperaName());
            pointRecList.add(pointRecord);
        }
        return pointRecList;
    }

    @Override
    public AnnounceListResponseDTO getAnnounceList(AnnounceListRequestDTO req) {
        QueryWrapper<OperaRecord> announceWrapper = new QueryWrapper<>();
        announceWrapper.eq("opera_id", 9).orderByDesc("time_created");  //最近发布优先
        List<OperaRecord> operaRecordList = queryRecordInPage(announceWrapper, req.getPageNum(), req.getPageSize());
        List<AnnounceInfo> announceInfoList = parseAnnounceInfo(operaRecordList);
        int pageCount = announceInfoList.size();
        int totalCount = recordMapper.selectCount(announceWrapper).intValue();
        logger.info("get announce list: pageCount:{}, totalCount:{}, record:{}", pageCount, totalCount, announceInfoList);

        return new AnnounceListResponseDTO(announceInfoList, pageCount, totalCount, "success");
    }

    @Override
    public AnnounceResponseDTO announce(AnnounceRequestDTO req, String masterId) throws UltraViresException {
        if(isNormalUser(masterId))
            throw new UltraViresException(masterId);
        OperaRecord announceRecord = new OperaRecord(masterId, 9, req.getContent());
        int insert = recordMapper.insert(announceRecord);
        logger.info("Insert {} data in opera_record table. announceRecord:{}", insert, announceRecord);
        return new AnnounceResponseDTO("success");
    }

    @Override
    public ScoreboardDTO getScoreboard() {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("point");
        List<UserInfo> userInfoList = queryUserInPage(wrapper,1, 10); //前十名
        List<RankInfo> rankInfoList = parseUserRankInfo(userInfoList);
        return new ScoreboardDTO(rankInfoList, "success");
    }

    @Override
    public SelfRankDTO getSelfRank(String userId) {
        int rank = userMapper.getRowNumberByUserId(userId);
        logger.info("user:{} rank of total scoreboard:{}", userId, rank);
        return new SelfRankDTO(rank, "success");
    }

    @Override
    public RecordOfPointResponseDTO getRecListOfPoint(RecordOfPointRequestDTO req, String userId) {
        QueryWrapper<OperaRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .between("opera_id", 1,7)
                .ne("opera_id",2)
                .orderByDesc("time_created");
        List<OperaRecord> operaRecList = queryRecordInPage(wrapper, req.getPageNum(), req.getPageSize());
        List<PointRecord> pointRecList = parsePointRecord(operaRecList);
        int pageCount = pointRecList.size();
        int totalCount = recordMapper.selectCount(wrapper).intValue();
        logger.info("Record list of point:{}",pointRecList);
        logger.info("pageCount:{}, totalCount:{}", pageCount, totalCount);

        return new RecordOfPointResponseDTO(pointRecList, pageCount, totalCount, "success");
    }


}
