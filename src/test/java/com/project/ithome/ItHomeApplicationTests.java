package com.project.ithome;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.ithome.entity.ResInfo;
import com.project.ithome.entity.Role;
import com.project.ithome.entity.UserInfo;
import com.project.ithome.mapper.ResourceMapper;
import com.project.ithome.mapper.UserMapper;
import com.project.ithome.util.RandomAccountUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ItHomeApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(ItHomeApplicationTests.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    void testRegister() {
        RandomAccountUtil randomAccountUtil = new RandomAccountUtil();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(randomAccountUtil.randomDigitNumber(9));
        userInfo.setUserName("WolverineX23");
        userInfo.setLevel(0);
        userInfo.setPoint(0);
        userInfo.setPassword("123456");
        userInfo.setEmail("");
        userInfo.setInterestTag(null);
        userInfo.setRole(Role.StationMaster);

        int insert = userMapper.insert(userInfo);
        System.out.println(insert);
        System.out.println(userInfo);
    }

    @Test
    void testGetTagFromDatabase() {
        UserInfo user = userMapper.selectById("834139070");
        logger.info("user info: {}", user);
        // autoResultMap = true 方法可行，从数据库读取成功，不为null
        // 在对应xml设置resultMap中对应的result 字段添加typeHandler属性无用（可能是自己问题）, 虽可将数据保存至数据库，但读取为null
        System.out.println(user.getInterestTag());

        ResInfo resource = resourceMapper.selectById("741528517155");
        logger.info("resource info: {}", resource);
        System.out.println(resource.getTechTag());
    }

    @Test
    void testQueryResForPage() {
        List<String> techTagRequest = new ArrayList<>();     //根据选择的标签进行筛选资源
        techTagRequest.add("书籍");
        techTagRequest.add("Java语言");
        QueryWrapper<ResInfo> wrapper = new QueryWrapper<>();
        /*还有一个条件，resInfo.getTechTag().containsAll(techTagRequest) 怎么决解？
            用like + and的sql语句组合可解决 缺陷：
            模糊查询，如：查询Java，会将Javascript标签一同查询出来，故需要人工被迫避免该类问题。
            但该分页查询业务能在sql（数据库）层面解决，很大避免了JVM内存浪费，保证了查询效率
         */
        wrapper.eq("status", "Pending")
                .like("tech_tag", techTagRequest.get(0))
                .like("tech_tag", techTagRequest.get(1));
        IPage<ResInfo> resPage = new Page<>(2,2);
        resPage = resourceMapper.selectPage(resPage, wrapper);
        logger.info("resource page: {}", resPage);
        List<ResInfo> resList = resPage.getRecords();

        logger.info("resource list: {}", resList);
        for(ResInfo res : resList) {
            System.out.println(res);
        }
    }
}
