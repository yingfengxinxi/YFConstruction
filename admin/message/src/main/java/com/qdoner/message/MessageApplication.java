package com.qdoner.message;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.kakarote.core.CoreApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhangzhiwei
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CoreApplication.class, MessageApplication.class})
@ComponentScan(
        basePackageClasses = {CoreApplication.class, MessageApplication.class})
@MapperScan(basePackages = {"com.qdoner.*.mapper", "com.kakarote.*.mapper"})
@EnableMethodCache(basePackages = "com.qdoner.message", order = -9999)
@EnableCreateCacheAnnotation
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
