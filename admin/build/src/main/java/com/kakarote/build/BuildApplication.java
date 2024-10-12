package com.kakarote.build;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.kakarote.core.CoreApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = {CoreApplication.class,BuildApplication.class})
@ComponentScan(basePackageClasses = {CoreApplication.class,BuildApplication.class})
@MapperScan(basePackages = {"com.kakarote.*.mapper","com.kakarote.build.*.mapper"})
@EnableMethodCache(basePackages = "com.kakarote.build",order = -9999)
@EnableCreateCacheAnnotation
@EnableScheduling
//@ServletComponentScan
public class BuildApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildApplication.class, args);
    }

}
