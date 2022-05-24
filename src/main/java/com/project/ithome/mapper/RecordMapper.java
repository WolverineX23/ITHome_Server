package com.project.ithome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.ithome.entity.OperaRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordMapper extends BaseMapper<OperaRecord> {
    //sql获取积分记录等
}
