package com.project.ithome.service.resource;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.ithome.entity.OperaInfo;
import com.project.ithome.mapper.OperationMapper;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, OperaInfo> implements OperationService {
}
