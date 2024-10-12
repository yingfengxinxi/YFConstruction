package com.qdoner.dataagg;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.kakarote.core.CoreApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wudw
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CoreApplication.class, DataaggApplication.class})
@ComponentScan(
        basePackageClasses = {CoreApplication.class, DataaggApplication.class})
@MapperScan(basePackages = {"com.qdoner.*.mapper", "com.kakarote.*.mapper"})
@EnableMethodCache(basePackages = "com.qdoner.dataagg", order = -9999)
@EnableCreateCacheAnnotation
@EnableScheduling
public class DataaggApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataaggApplication.class, args);
    }
}
