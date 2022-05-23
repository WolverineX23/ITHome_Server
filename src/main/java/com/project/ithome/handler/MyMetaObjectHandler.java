package com.project.ithome.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.project.ithome.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component          //把普通pojo实例化到spring容器中,配置一个bean，相当于<bean id="" class="">
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    //插入操作自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("timeCreated", LocalDateTime.now());
        metaObject.setValue("timeModified", LocalDateTime.now());
        /*
        自动填充创建这条数据的用户名
        metaObject.setValue("userCreated", BaseContext.getCurrentUserId());
        metaObject.setValue("userModified", BaseContext.getCurrentUserId());
         */
    }

    //更新操作自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("当前线程id{}", Thread.currentThread().getId());
        metaObject.setValue("timeModified", LocalDateTime.now());
        /*
        自动填充修改这条数据的用户名
        metaObject.setValue("userModified", BaseContext.getCurrentUserId());
         */
    }
}

/*
在可以获取到用户名的方法中调用我们封装的工具类将用户名放入ThreadLocal
String userId = (String) request.getSession().getAttribute("userId");
BaseContext.setCurrentUserId(userId);
 */