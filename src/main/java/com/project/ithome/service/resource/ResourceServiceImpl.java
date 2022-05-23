package com.project.ithome.service.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.ithome.dto.administration.*;
import com.project.ithome.dto.resource.*;
import com.project.ithome.entity.*;
import com.project.ithome.exception.resource.ResourceNotFoundException;
import com.project.ithome.exception.resource.UltraViresException;
import com.project.ithome.mapper.RecordMapper;
import com.project.ithome.mapper.ResourceMapper;
import com.project.ithome.mapper.UserMapper;
import com.project.ithome.util.RandomAccountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResInfo> implements ResourceService {
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    private final ResourceMapper resourceMapper;
    private final RecordMapper recordMapper;
    private final UserMapper userMapper;

    public ResourceServiceImpl(ResourceMapper resourceMapper, RecordMapper recordMapper, UserMapper userMapper) {
        this.resourceMapper = resourceMapper;
        this.recordMapper = recordMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean isResIdExisted(String resId) {
        ResInfo resInfo = resourceMapper.selectById(resId);
        if(resInfo == null) {
            logger.info("resource:{} is nonexistent", resId);
            return false;
        }
        logger.info("resource:{} is existed", resId);
        return true;
    }

    @Override
    public List<ResourceResume> parseResResume(List<ResInfo> resList) {
        List<ResourceResume> resResumeList = new ArrayList<>();
        for (ResInfo resInfo : resList) {                                     //压缩传送给前端的数据（去除该业务无用数据）
            ResourceResume resourceResume = new ResourceResume();
            resourceResume.setResId(resInfo.getResId());
            resourceResume.setResName(resInfo.getResName());
            resourceResume.setResDescription(resInfo.getResDescription());
            resourceResume.setColAmount(resInfo.getColAmount());
            resourceResume.setTechTag(resInfo.getTechTag());
            resourceResume.setResLink(resInfo.getResLink());
            resResumeList.add(resourceResume);
        }
        return resResumeList;
    }

    @Override
    public List<ResInfo> queryResInPage(QueryWrapper<ResInfo> wrapper, int pageNum, int size) {
        IPage<ResInfo> resPage = new Page<>(pageNum, size);
        resPage = resourceMapper.selectPage(resPage, wrapper);
        logger.info("resource page: {}", resPage);
        List<ResInfo> resList = resPage.getRecords();
        logger.info("resource list: {}", resList);
        return resList;
    }

    @Override
    public boolean isUserUltraVires(String userId) {
        //判断访问该接口的用户是否有权限
        UserInfo user = userMapper.selectById(userId);
        Role role = user.getRole();
        if(role.equals(Role.NormalUser)) {
            logger.info("User{} with role of {} do not have access to the api.", userId, role);
            return true;
        }
        return false;
    }

    @Override
    public ResRecommendResponseDTO recommendResource(ResRecommendRequestDTO recommendInfo, String userId) {
        //根据链接（唯一键）判断资源是否存在且status = passed  ——自动过滤
        QueryWrapper<ResInfo> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("res_link", recommendInfo.getResLink())
                .eq("status", Status.Passed);
        List<ResInfo> sameResource = resourceMapper.selectList(resourceQueryWrapper);
        if(sameResource.isEmpty()) {
            logger.info("No resource in database is as same as the recommended resource");
            boolean isExisted = true;
            String newId = "";
            while(isExisted){                      //生成唯一的resId
                newId = RandomAccountUtil.randomDigitNumber(12);
                isExisted = isResIdExisted(newId);
            }
            logger.info("Unique resId generated: {}", newId);
            ResInfo newResource = new ResInfo(newId, recommendInfo.getResName(), recommendInfo.getResLink(), recommendInfo.getTechTag(), 0, 0, Status.Pending, recommendInfo.getResDescription(), recommendInfo.getResDetail());
            int insertResource = resourceMapper.insert(newResource);
            logger.info("{} resource data had been inserted in table res_info.", insertResource);

            OperaRecord recommendRecord = new OperaRecord(userId, 2, newId, "推荐资源");
            int insertRecord = recordMapper.insert(recommendRecord);
            logger.info("{} recommend record had been inserted in table opera_record", insertRecord);
            //推荐完成后，传回资源信息——可预览
            return new ResRecommendResponseDTO(newResource, userId, RecommendResult.Pass, "success");
        }
        else {
            logger.info("A resource similar to the recommended resource already exists in the database.");
            QueryWrapper<OperaRecord> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.eq("res_id", sameResource.get(0).getResId())
                    .eq("opera_id", 7);
            List<OperaRecord> record = recordMapper.selectList(recordQueryWrapper);
            logger.info("the record of the similar resource: {}", record);
            String recommender = record.get(0).getUserId();
            return new ResRecommendResponseDTO(sameResource.get(0), recommender, RecommendResult.Deny, "RecommendError: A resource similar to the recommended resource already exists.");
        }

    }

    @Override
    public ResGetByTechTagResponseDTO getPassedResByTagArray(ResGetByTechTagRequestDTO queryInfo) {
        List<String> tagArray = queryInfo.getTechTag();
        int tagCount = tagArray.size();

        QueryWrapper<ResInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "Passed");   //资源已被共享到平台
        for (int i = 0; i < tagCount; i++) {
            wrapper.like("tech_tag", tagArray.get(i));
        }
        List<ResInfo> resList = queryResInPage(wrapper, queryInfo.getPageNum(), queryInfo.getPageSize());
        int pageCount = resList.size();
        List<ResourceResume> resResumeList = parseResResume(resList);   // resource -- resResume
        int totalCount = resourceMapper.selectCount(wrapper).intValue();  //资源总量
        return new ResGetByTechTagResponseDTO(resResumeList, pageCount, totalCount, "success");
    }

    @Override
    public PassedResInfoDTO getPassedResInfoById(String resId) throws ResourceNotFoundException {
        ResInfo resInfo = resourceMapper.selectById(resId);
        logger.info("resource info of id{}, {}", resId, resInfo);
        if(resInfo == null)
            throw new ResourceNotFoundException(resId);

        resInfo.setViews(resInfo.getViews() + 1);       //浏览量+1
        int update = resourceMapper.updateById(resInfo);        //更新浏览量
        logger.info("{} resource data was updated.", update);
        QueryWrapper<OperaRecord> recordWrapper = new QueryWrapper<>();
        recordWrapper.eq("opera_id", 7)
                .eq("res_id", resId);
        List<OperaRecord> record = recordMapper.selectList(recordWrapper);
        String recommenderId = record.get(0).getUserId();
        LocalDateTime passedTime = record.get(0).getTimeCreated();
        UserInfo user = userMapper.selectById(recommenderId);
        String recommenderName = user.getUserName();
        PassedResInfoDTO resInfoDTO = new PassedResInfoDTO(resInfo, recommenderId, recommenderName, passedTime, "success");
        logger.info("resInfoDTO");

        return resInfoDTO;
    }

    @Override
    public ResSearchResponseDTO searchRes(ResSearchRequestDTO searchInfo, String content) {
        QueryWrapper<ResInfo> resWrapper = new QueryWrapper<>();
        resWrapper.eq("status", Status.Passed)
                .like("res_name", content)          //(sql-like模糊搜索)
                .orderByDesc("col_amount");         //默认按照收藏量降序排列
        //先判断是何种搜索
        //全站搜索
        if(searchInfo.getTagArray().isEmpty()) {
            logger.info("Total station search: {}", searchInfo);
        }
        else{       //基于技术标签集合的搜索/专栏搜索
            int tagCount = searchInfo.getTagArray().size();
            for(int i=0; i< tagCount; i++) {
                resWrapper.like("tech_tag", searchInfo.getTagArray().get(i));
            }
            logger.info("Column search: {}", searchInfo);
        }
        int totalCount = resourceMapper.selectCount(resWrapper).intValue();        //相关资源总量
        List<ResInfo> resList = queryResInPage(resWrapper, searchInfo.getPageNum(), searchInfo.getPageSize());
        int pageCount = resList.size();             //相关资源当前页数量
        List<ResourceResume> resResumeList = parseResResume(resList);
        ResSearchResponseDTO resSearchResponseDTO = new ResSearchResponseDTO(resResumeList, pageCount, totalCount, "success");
        logger.info("search result: {}", resSearchResponseDTO);
        return resSearchResponseDTO;
    }

    @Override
    public PendingResPageResponseDTO getPendingResPage(PendingResPageRequestDTO requestInfo, String userId) throws UltraViresException {
        //判断访问该接口的用户是否有权限
        if(isUserUltraVires(userId)) {
            logger.info("User{} do not have access to the api.", userId);
            throw new UltraViresException(userId);
        }

        QueryWrapper<ResInfo> resWrapper = new QueryWrapper<>();
        resWrapper.eq("status", Status.Pending).orderByDesc("time_created");   //按照推荐时间倒序，最新推荐在前
        List<ResInfo> pendingResList = queryResInPage(resWrapper, requestInfo.getPageNum(), requestInfo.getPageSize());
        List<ResourceResume> pendingResResumeList = parseResResume(pendingResList);
        int pageCount = pendingResResumeList.size();
        int totalCount = resourceMapper.selectCount(resWrapper).intValue();   //selectCount!!!
        PendingResPageResponseDTO pendingResPageResponseDTO = new PendingResPageResponseDTO(pendingResResumeList, pageCount, totalCount, "success");
        logger.info("Pending Resource Info in page {}: {}", requestInfo.getPageNum(), pendingResPageResponseDTO);
        return pendingResPageResponseDTO;
    }

    @Override
    public PendingResInfoDTO getPendingResInfoById(String resId, String userId) throws ResourceNotFoundException, UltraViresException {
        if(isUserUltraVires(userId)) {
            logger.info("User{} do not have access to the api.", userId);
            throw new UltraViresException(userId);
        }

        ResInfo resInfo = resourceMapper.selectById(resId);
        logger.info("resource info of id{}, {}", resId, resInfo);
        if(resInfo == null)
            throw new ResourceNotFoundException(resId);

        QueryWrapper<OperaRecord> recordWrapper = new QueryWrapper<>();
        recordWrapper.eq("opera_id", 2)                         //operaId = 2
                .eq("res_id", resId);
        List<OperaRecord> record = recordMapper.selectList(recordWrapper);
        String recommenderId = record.get(0).getUserId();
        UserInfo user = userMapper.selectById(recommenderId);
        String recommenderName = user.getUserName();
        PendingResInfoDTO pendingResInfoDTO = new PendingResInfoDTO(resInfo.getResName(),
                resInfo.getResDescription(), resInfo.getResLink(), resInfo.getTechTag(), resInfo.getResDetail(),
                recommenderId, recommenderName, "success");
        logger.info("PendingResInfoDTO:{}",pendingResInfoDTO);
        return pendingResInfoDTO;
    }

    @Override
    public ExamineResResponseDTO examineRes(ExamineResRequestDTO examineInfo, String resId, String adminId) throws UltraViresException {
        UserInfo admin = userMapper.selectById(adminId);
        Role role = admin.getRole();
        if(role.equals(Role.NormalUser)) {
            logger.info("User{} with role of {} do not have access to the api.", adminId, role);
            throw new UltraViresException(adminId);
        }

        //审核人信息更新
        admin.setPoint(admin.getPoint() + 2);       //审核资源point+2
        if(admin.getPoint() / 30 != admin.getLevel())
            admin.setLevel(admin.getLevel() + 1);
        int updateAdmin = userMapper.updateById(admin);
        logger.info("Update {} data about admin who:{} do a examination.", updateAdmin,adminId);

        //审核记录插入
        OperaRecord examineRecord = new OperaRecord(adminId, 5, resId, "管理员审核该资源");
        int examineInsert = recordMapper.insert(examineRecord);
        logger.info("Insert {} data about a record of examining pending resource.", examineInsert);

        ResInfo examineRes = resourceMapper.selectById(resId);
        UpdateWrapper<ResInfo> resInfoUpdateWrapper = new UpdateWrapper<>();
        resInfoUpdateWrapper.eq("res_id", resId);       //需加上，以对应某条数据，不然会改动整个表
        OperaRecord examineResultRec;
        if(examineInfo.getResult() == ExamineResult.Pass) {                 //若审核通过
            //更新被审核的资源的状态
            resInfoUpdateWrapper.set("status", Status.Passed);
            //插入审核结果的记录（结果、原因）
            examineResultRec = new OperaRecord(examineInfo.getRecommenderId(), 7, resId, examineInfo.getReasonDesc());
            //更新成功推荐者的积分和等级信息
            UserInfo recommender = userMapper.selectById(examineInfo.getRecommenderId());
            recommender.setPoint(recommender.getPoint() + 10);      //point + 10
            if(recommender.getPoint() / 30 != recommender.getLevel())
                recommender.setLevel(recommender.getLevel() + 1);   //recommender.setLevel(recommender.getPoint() / 30);
            int updateRecommender = userMapper.updateById(recommender);
            logger.info("Update {} data about recommender who:{} recommended a resource successfully.", updateRecommender, examineInfo.getRecommenderId());
        }
        else{                                                               //若审核拒绝
            //更新被审核的资源的状态
            resInfoUpdateWrapper.set("status", Status.Deny);
            //插入审核结果的记录（结果、原因）
            examineResultRec = new OperaRecord(examineInfo.getRecommenderId(), 8, resId, examineInfo.getReasonDesc());
        }
        //更新被审核的资源的状态
        int updateRes = resourceMapper.update(examineRes, resInfoUpdateWrapper);    //new ResInfo()第一个参数的问题？： 更新后，数据库中该条数据中int类型字段值清零
        logger.info("Update {} data about resource status", updateRes);
        //插入审核结果的记录（结果、原因）
        int examineResultInsert = recordMapper.insert(examineResultRec);
        logger.info("Insert {} data about a record of a resource{} get a result:{}", examineResultInsert, resId, examineInfo.getResult());
        return new ExamineResResponseDTO("success");
    }
}
