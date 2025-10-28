package com.small.dragon.yml;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 叶旻龙
 */
@EnableAsync
@MapperScan("com.smalldragon.yml.*.mapper")
@SpringBootApplication(scanBasePackages = "com.smalldragon.yml")
public class YmlServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YmlServerApplication.class, args);
    }

}
