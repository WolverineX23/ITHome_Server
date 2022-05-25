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
import com.project.ithome.mapper.*;
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
    private final EvaInfoMapper evaInfoMapper;
    private final EvaMapMapper evaMapMapper;

    public ResourceServiceImpl(ResourceMapper resourceMapper, RecordMapper recordMapper, UserMapper userMapper, EvaInfoMapper evaInfoMapper, EvaMapMapper evaMapMapper) {
        this.resourceMapper = resourceMapper;
        this.recordMapper = recordMapper;
        this.userMapper = userMapper;
        this.evaInfoMapper = evaInfoMapper;
        this.evaMapMapper = evaMapMapper;
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
    public boolean isEvaIdExisted(String evaId) {
        EvaInfo evaInfo = evaInfoMapper.selectById(evaId);
        if(evaInfo == null) {
            logger.info("evaluation:{} is nonexistent", evaId);
            return false;
        }
        logger.info("evaluation:{} is existed", evaId);
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
    public List<EvaluationDTO> parseEvaluationDTO(List<EvaMap> evaMapList) {
        List<EvaluationDTO> evaluationDTOList = new ArrayList<>();
        for(EvaMap evaMap : evaMapList) {
            String evaId = evaMap.getEvaId();
            String userId = evaMap.getUserId();
            EvaInfo evaInfo = evaInfoMapper.selectById(evaId);
            UserInfo userInfo = userMapper.selectById(userId);
            EvaluationDTO evaluationDTO = new EvaluationDTO(userId, userInfo.getUserName(), evaInfo.getStar(), evaInfo.getComment(), evaInfo.getTimeCreated());
            evaluationDTOList.add(evaluationDTO);
        }
        return evaluationDTOList;
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
    public List<OperaRecord> queryRecordInPage(QueryWrapper<OperaRecord> wrapper, int pageNum, int size) {
        IPage<OperaRecord> recordPage = new Page<>(pageNum, size);
        recordPage = recordMapper.selectPage(recordPage, wrapper);
        List<OperaRecord> recordList = recordPage.getRecords();
        logger.info("record list: {}", recordList);
        return recordList;
    }

    @Override
    public List<EvaMap> queryEvaMapInPage(QueryWrapper<EvaMap> wrapper, int pageNum, int size) {
        IPage<EvaMap> evaMapPage = new Page<>(pageNum, size);
        evaMapPage = evaMapMapper.selectPage(evaMapPage, wrapper);
        List<EvaMap> evaMapList = evaMapPage.getRecords();
        logger.info("evaMap list: {}", evaMapList);
        return evaMapList;
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

        QueryWrapper<ResInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "Passed");   //资源已被共享到平台
        for (String tag : tagArray) {
            wrapper.like("tech_tag", tag);
        }
        List<ResInfo> resList = queryResInPage(wrapper, queryInfo.getPageNum(), queryInfo.getPageSize());
        int pageCount = resList.size();
        List<ResourceResume> resResumeList = parseResResume(resList);   // resource -- resResume
        int totalCount = resourceMapper.selectCount(wrapper).intValue();  //资源总量
        return new ResGetByTechTagResponseDTO(resResumeList, pageCount, totalCount, "success");
    }

    @Override
    public PassedResInfoResponseDTO getPassedResInfoById(PassedResInfoRequestDTO req, String resId) throws ResourceNotFoundException {
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

        QueryWrapper<EvaMap> evaMapWrapper = new QueryWrapper<>();
        evaMapWrapper.eq("res_id", resId).orderByDesc("time_created"); //从最新评论开始
        List<EvaMap> evaMapList = queryEvaMapInPage(evaMapWrapper, req.getPageNum(), req.getPageSize());
        List<EvaluationDTO> evaluationList = parseEvaluationDTO(evaMapList);
        int pageCount = evaluationList.size();
        int totalCount = evaMapMapper.selectCount(evaMapWrapper).intValue();
        logger.info("get evaluations:{}", evaluationList);
        logger.info(" pageCount:{}, totalCount:{}", pageCount, totalCount);


        PassedResInfoResponseDTO resInfoDTO = new PassedResInfoResponseDTO(resInfo, recommenderId, recommenderName, passedTime,evaluationList, pageCount, totalCount, "success");
        logger.info("getPassedResInfoById:{}",resInfoDTO);

        return resInfoDTO;
    }

    @Override
    public QueryWrapper<ResInfo> getResWrapper(List<String> tagArr, OrderAttr orderAttr, String content) {
        QueryWrapper<ResInfo> resWrapper = new QueryWrapper<>();
        resWrapper.eq("status", Status.Passed);

        if(!content.equals(""))
            resWrapper.like("res_name", content);   //若为空， 仍考虑排序和筛选因素

        //结合排序
        if(orderAttr.equals(OrderAttr.Time))   //默认按资源发布时间排序
            resWrapper.orderByDesc("time_modified");    //为通过时间  默认新资源在前
        else if(orderAttr.equals(OrderAttr.Views))  //按浏览量排序
            resWrapper.orderByDesc("views");
        else if(orderAttr.equals(OrderAttr.ColAmount))
            resWrapper.orderByDesc("col_amount");
        else{
            //按照评价（平均星级）
            logger.info("Order by {}", OrderAttr.Evaluation);
        }

        //结合标签筛选
        if(!tagArr.isEmpty()) {
            int tagCount = tagArr.size();
            for(int i=0; i< tagCount; i++) {
                resWrapper.like("tech_tag", tagArr.get(i));
            }
        }

        return resWrapper;
    }

    @Override
    public ResTotalSearchResponseDTO totalSearchRes(ResTotalSearchRequestDTO searchInfo, String content) {

        QueryWrapper<ResInfo> resWrapper = getResWrapper(searchInfo.getTagArray(), searchInfo.getOrderAttr(), content);
        int totalCount = resourceMapper.selectCount(resWrapper).intValue();        //相关资源总量
        List<ResInfo> resList = queryResInPage(resWrapper, searchInfo.getPageNum(), searchInfo.getPageSize());
        int pageCount = resList.size();             //相关资源当前页数量
        List<ResourceResume> resResumeList = parseResResume(resList);
        ResTotalSearchResponseDTO resTotalSearchResponseDTO = new ResTotalSearchResponseDTO(resResumeList, pageCount, totalCount, "success");
        logger.info("total search result: {}", resTotalSearchResponseDTO);
        return resTotalSearchResponseDTO;
    }

    @Override
    public ResColSearchResponseDTO colSearchRes(ResColSearchRequestDTO searchInfo, String tag, String content) {
        QueryWrapper<ResInfo> resWrapper = getResWrapper(searchInfo.getTagArray(), searchInfo.getOrderAttr(), content);
        resWrapper.like("tech_tag", tag);   //附加专栏自带标签
        int totalCount = resourceMapper.selectCount(resWrapper).intValue();        //相关资源总量
        List<ResInfo> resList = queryResInPage(resWrapper, searchInfo.getPageNum(), searchInfo.getPageSize());
        int pageCount = resList.size();             //相关资源当前页数量
        List<ResourceResume> resResumeList = parseResResume(resList);
        ResColSearchResponseDTO resColSearchResponseDTO = new ResColSearchResponseDTO(resResumeList, pageCount, totalCount, "success");
        logger.info("column search result: {}", resColSearchResponseDTO);
        return resColSearchResponseDTO;
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

            //更新该条资源的time_modified字段
            resourceMapper.updateModifiedTimeInRes(LocalDateTime.now(), resId);
            logger.info("Update the modified_time to the passed_time of the res{}.", resId);
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

    @Override
    public EvaInfoResponseDTO evaluateRes(EvaInfoRequestDTO req, String resId, String userId) {
        boolean isExisted = true;
        String evaId = "";
        while (isExisted) {                                               //生成唯一的evaId    14位
            evaId = RandomAccountUtil.randomDigitNumber(14);
            isExisted = isEvaIdExisted(evaId);
        }
        logger.info("Unique evaId generated: {}", evaId);

        //判断该用户该次评价是否为当日首次评价
        QueryWrapper<OperaRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("opera_id", 3).orderByDesc("time_created");
        List<OperaRecord> evaRecord = queryRecordInPage(wrapper, 1, 1);     //查询最新一条评价记录
        boolean isFirstEvaPerDay = true;
        if(!evaRecord.isEmpty()) {
            int year = evaRecord.get(0).getTimeCreated().getYear();
            int dayOfYear = evaRecord.get(0).getTimeCreated().getDayOfYear();
            LocalDateTime time = LocalDateTime.now();
            if(year == time.getYear() && dayOfYear == time.getDayOfYear()) {
                isFirstEvaPerDay = false;
                logger.info("Is not the first evaluation.");
            }
        }
        //若为当日首次评论，插入记录并更新积分
        if(isFirstEvaPerDay) {
            //插入每日首次评价记录
            OperaRecord evaPerDayRecord = new OperaRecord(userId, 3, resId, "每日资源评价");
            int recordInsert = recordMapper.insert(evaPerDayRecord);
            logger.info("Insert {} data in opera_record table. evaPerDayRecord:{}", recordInsert, evaPerDayRecord);
            //更新积分
            UserInfo user = userMapper.selectById(userId);
            user.setPoint(user.getPoint() + 2);             //+2分
            if(user.getPoint() / 30 != user.getLevel())
                user.setLevel(user.getLevel() + 1);         //等级
            int userUpdate = userMapper.updateById(user);
            logger.info("Update {} data in user_info. userId:{}, point:{}, level:{}",userUpdate, userId, user.getPoint(), user.getLevel());
        }
        //插入评价数据
        EvaInfo evaInfo = new EvaInfo(evaId, req.getStar(), req.getComment());
        int evaInfoInsert = evaInfoMapper.insert(evaInfo);
        logger.info("Insert {} data in eva_info table. evaInfo:{}", evaInfoInsert, evaInfo);
        //插入评价映射关系
        EvaMap evaMap = new EvaMap(userId, resId, evaId);
        int evaMapInsert = evaMapMapper.insert(evaMap);
        logger.info("Insert {} data in eva_map table. evaMap:{}", evaMapInsert, evaMap);

        return new EvaInfoResponseDTO("success");
    }
}
