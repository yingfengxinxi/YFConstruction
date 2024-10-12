package com.kakarote.core;

import org.mybatis.spring.annotation.MapperScan;

/**
 * @author zhangzhiwei
 */
@MapperScan(basePackages = {"com.kakarote.*.mapper"})
public class CoreApplication {

	public static void main(String[] args) {
		System.out.println("core");
	}
}
