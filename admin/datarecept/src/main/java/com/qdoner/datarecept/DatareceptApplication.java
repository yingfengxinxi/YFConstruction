package com.qdoner.datarecept;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.kakarote.core.CoreApplication;
import com.qdoner.datarecept.server.SocketServer;
import com.qdoner.datarecept.server.TcpServer;
import com.qdoner.datarecept.server.UdpServer;
import io.netty.channel.ChannelFuture;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wudw
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {CoreApplication.class, DatareceptApplication.class})
@ComponentScan(
        basePackageClasses = {CoreApplication.class, DatareceptApplication.class})
@MapperScan(basePackages = {"com.qdoner.**.mapper", "com.kakarote.*.mapper"})
@EnableMethodCache(basePackages = "com.qdoner.datarecept", order = -9999)
@EnableCreateCacheAnnotation
//@EnableScheduling
public class DatareceptApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(DatareceptApplication.class, args);
//        run.getBean(SocketServer.class).start();
    }


    @Autowired
    private TcpServer tcpServer;

    @Autowired
    private UdpServer udpServer;

    /**
     * 启动报文接收
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        tcpServer.createChannel();
        udpServer.createChannel();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                tcpServer.closeChannels();
                udpServer.closeChannels();
            }
        });
    }
}
