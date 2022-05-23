package com.project.ithome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.project.ithome.mapper")
public class ItHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItHomeApplication.class, args);
    }

}
