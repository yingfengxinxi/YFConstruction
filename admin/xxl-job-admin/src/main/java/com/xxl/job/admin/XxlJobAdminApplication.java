package com.xxl.job.admin;

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
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CoreApplication.class, XxlJobAdminApplication.class})
@ComponentScan(
		basePackageClasses = {CoreApplication.class, XxlJobAdminApplication.class})
@MapperScan(basePackages = {"com.xxl.job.admin.mapper", "com.kakarote.*.mapper",})
@EnableCreateCacheAnnotation
public class XxlJobAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(XxlJobAdminApplication.class, args);
	}

}