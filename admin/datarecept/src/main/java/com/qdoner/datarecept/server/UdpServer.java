package com.qdoner.datarecept.server;

import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;
import com.qdoner.datarecept.service.IEquipmentReceptPlatformService;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * udp服务端
 *
 * @author wudw
 */
@Slf4j
@Component
public class UdpServer {

    @Autowired
    private UdpServerHandler udpServerHandler;

    private Map<Integer, Channel> ChannelMap = new HashMap<>();

    private Map<Integer, EventLoopGroup> GroupMap = new HashMap<>();

    @Autowired
    private IEquipmentReceptPlatformService receptPlatformService;
    /**
     * 建立通道：初始
     *
     * @param
     */
    public void createChannel() {
        List<EquipmentReceptPlatform> list = receptPlatformService.lambdaQuery().eq(EquipmentReceptPlatform::getStatus, "1")
                .eq(EquipmentReceptPlatform::getType, "2")
                .isNotNull(EquipmentReceptPlatform::getTenantId)
                .list();
        for (EquipmentReceptPlatform platform : list) {
            log.info("UDP server port =>" + platform.getPort());
            createChannel(platform.getPort());
        }
    }
    /**
     * 建立通道：端口
     *
     * @param
     */
    public void createChannel(int port) {
        ChannelFuture f = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            bootstrap.group(group)
                    // 主线程处理
                    .channel(NioDatagramChannel.class)
                    // 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    // 设置读缓冲区为2M
                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
                    // 设置写缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {

                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NioEventLoopGroup(), udpServerHandler);
                        }
                    });

            f = bootstrap.bind(port).sync();
            Channel channel = f.channel();
            //储存
            ChannelMap.put(port, channel);
            GroupMap.put(port, group);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("[UDP]Netty server on port " + port + " and ready for connections...");
            } else {
                log.error("[UDP]Netty server start up Error!");
            }
        }
    }
    /**
     * 关闭通道：销毁服务（destroy）
     */
    public void closeChannels() {
        for (Map.Entry<Integer, Channel> entry : ChannelMap.entrySet()) {
            closeChannels(entry.getKey());
        }
    }
    /**
     * 关闭通道：端口
     */
    public void closeChannels(int port) {
        Channel channel = ChannelMap.get(port);
        if (channel != null) {
            channel.close();
            ChannelMap.remove(port);
        }
        EventLoopGroup group = GroupMap.get(port);
        if (group != null) {
            group.shutdownGracefully();
            GroupMap.remove(port);
        }
        log.info("[UDP]Netty server listening on port " + port + " Shutdown Success!");
    }
}
