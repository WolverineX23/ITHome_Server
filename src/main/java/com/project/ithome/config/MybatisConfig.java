package com.project.ithome.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
//@MapperScan(basePackages = "com.project.ithome.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfig {
    /*
    static final String MAPPER_LOCATION = "classpath:mybatis/*.xml";

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private  String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource) throws Exception{
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
            .getResources(MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        DataSourceBuilder dataSource = DataSourceBuilder.create();
        dataSource.driverClassName(driverClass);
        dataSource.url(url);
        dataSource.username(username);
        dataSource.password(password);
        return dataSource.build();
    }
    */
}

