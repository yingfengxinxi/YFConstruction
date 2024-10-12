package com.qdoner.workflow;

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
 * @author wudw
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CoreApplication.class, WorkflowApplication.class})
@ComponentScan(basePackageClasses = {CoreApplication.class, WorkflowApplication.class})
@MapperScan(basePackages = {"com.qdoner.*.mapper","com.kakarote.*.mapper"})
@EnableMethodCache(basePackages = "com.qdoner.workflow",order = -9999)
@EnableCreateCacheAnnotation
public class WorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}
}
