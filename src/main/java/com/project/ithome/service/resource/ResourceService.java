package com.project.ithome.service.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.ithome.dto.administration.*;
import com.project.ithome.dto.resource.*;
import com.project.ithome.entity.EvaMap;
import com.project.ithome.entity.OperaRecord;
import com.project.ithome.entity.ResInfo;
import com.project.ithome.exception.resource.ResourceNotFoundException;
import com.project.ithome.exception.resource.UltraViresException;

import java.util.List;

public interface ResourceService extends IService<ResInfo> {

    //判断生成的resId是否存在
    boolean isResIdExisted(String resId);

    //判断生成的evaId是否存在
    boolean isEvaIdExisted(String evaId);

    //将ResInfo 转化为 ResourceResume
    List<ResourceResume> parseResResume(List<ResInfo> resInfoList);

    //将EvaMap 转化为 EvaluationDTO
    List<EvaluationDTO> parseEvaluationDTO(List<EvaMap> evaMapList);

    //资源分页查询
    List<ResInfo> queryResInPage(QueryWrapper<ResInfo> wrapper, int pageNum, int size);

    //记录分页查询
    List<OperaRecord> queryRecordInPage(QueryWrapper<OperaRecord> wrapper, int pageNum, int size);

    //评价映射信息分页查询
    List<EvaMap> queryEvaMapInPage(QueryWrapper<EvaMap> wrapper, int pageNum, int size);

    //判断用户是否越权
    boolean isUserUltraVires(String userId);

    //推荐资源
    ResRecommendResponseDTO recommendResource(ResRecommendRequestDTO recommendInfo, String userId);

    //根据技术标签集合获取相关资源
    ResGetByTechTagResponseDTO getPassedResByTagArray(ResGetByTechTagRequestDTO queryInfo);

    //根据资源ID获取该资源信息
    PassedResInfoResponseDTO getPassedResInfoById(PassedResInfoRequestDTO req, String resId) throws ResourceNotFoundException;

    //搜索——分页查询  条件构造器
    QueryWrapper<ResInfo> getResWrapper(List<String> tagArr, OrderAttr orderAttr, String content);

    //资源全站搜索
    ResTotalSearchResponseDTO totalSearchRes(ResTotalSearchRequestDTO searchInfo, String content);

    //资源专栏搜索
    ResColSearchResponseDTO colSearchRes(ResColSearchRequestDTO searchInfo, String tag, String content);

    //分页获取待审核资源
    PendingResPageResponseDTO getPendingResPage(PendingResPageRequestDTO requestInfo, String userId) throws UltraViresException;

    //获取某个待审资源的信息
    PendingResInfoDTO getPendingResInfoById(String resId, String userId) throws ResourceNotFoundException, UltraViresException;

    //审核资源
    ExamineResResponseDTO examineRes(ExamineResRequestDTO examineInfo, String resId, String adminId) throws UltraViresException;

    //评价资源
    EvaInfoResponseDTO evaluateRes(EvaInfoRequestDTO req, String resId, String userId);
}