package com.project.ithome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.ithome.entity.ResInfo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ResourceMapper extends BaseMapper<ResInfo> {

    LocalDateTime getPassedTimeByResId(String resId);

    void updateModifiedTimeInRes(LocalDateTime examineTime, String resId);
}
