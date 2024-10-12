package com.kakarote.build.wx.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
//import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
//import com.jfinal.plugin.druid.DruidPlugin;
//import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ActiveRecordPluginConfig {

    // 以下三个信息在 src/mian/resources/application.properties配置的数据库连接信息
    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    @Value("${spring.datasource.druid.url}")
    private String url;

//    @Bean
//    public ActiveRecordPlugin initActiveRecordPlugin() {
//
//        DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
//
//        // 加强数据库安全
//        WallFilter wallFilter = new WallFilter();
//
//        wallFilter.setDbType("mysql");
//
//        druidPlugin.addFilter(wallFilter);
//        // 添加 StatFilter 才会有统计数据
//        druidPlugin.addFilter(new StatFilter());
//
//        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
//
//        arp.setShowSql(true);
//
//        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
//
//        // sql文件路径在src/mian/resources/sql/all_sqls.sql
//        arp.addSqlTemplate("/sql/all_sqls.sql");
//
//        //	arp.addMapping("blog", Blog.class);
//
//        // 与 jfinal web 环境唯一的不同是要手动调用一次相关插件的start()方法
//        druidPlugin.start();
//        arp.start();
//
//        return arp;
//    }
}
