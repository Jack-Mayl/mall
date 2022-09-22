package com.longzai.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Map;

@SpringBootApplication
@MapperScan(basePackages = "com.longzai.mall.model.dao")
@EnableSwagger2
// 希望打开缓存功能
@EnableCaching
public class MallApplication {

    public static void main(String[] args) {
        System.setProperty("user.name", "root");
        for (Map.Entry<Object, Object> objectObjectEntry : System.getProperties().entrySet()) {
            System.out.println(objectObjectEntry.getKey()+"==="+objectObjectEntry.getValue());
        }
        SpringApplication.run(MallApplication.class, args);
    }

}
