package com.project.ithome.service.resource;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.ithome.entity.EvaInfo;
import com.project.ithome.mapper.EvaInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaInfoMapper, EvaInfo> implements EvaluationService{

}
