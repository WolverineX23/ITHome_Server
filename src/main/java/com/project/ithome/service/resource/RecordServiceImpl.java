package com.project.ithome.service.resource;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.ithome.entity.OperaRecord;
import com.project.ithome.mapper.RecordMapper;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, OperaRecord> implements RecordService {
}
