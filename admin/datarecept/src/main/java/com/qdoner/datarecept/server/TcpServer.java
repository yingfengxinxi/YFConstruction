package com.qdoner.datarecept.server;

import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;
import com.qdoner.datarecept.server.coder.HexDecoder;
import com.qdoner.datarecept.server.coder.HexEncoder;
import com.qdoner.datarecept.service.IEquipmentReceptPlatformService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2021-12-12 wudw
 */
@Slf4j
@Component
public class TcpServer {

    @Autowired
    private TcpServerHandler serverHandler;

    private Map<Integer, Channel> ChannelMap = new HashMap<>();

    private Map<Integer, EventLoopGroup> BossGroupMap = new HashMap<>();

    private Map<Integer, EventLoopGroup> WorkerGroupMap = new HashMap<>();

    @Autowired
    private IEquipmentReceptPlatformService receptPlatformService;

    /**
     * 建立通道：初始
     *
     * @param
     */
    public void createChannel() {
        List<EquipmentReceptPlatform> list = receptPlatformService.lambdaQuery().eq(EquipmentReceptPlatform::getStatus, "1")
                .eq(EquipmentReceptPlatform::getType, "1")
                .isNotNull(EquipmentReceptPlatform::getTenantId)
                .list();
        for (EquipmentReceptPlatform platform : list) {
            log.info("TCP server port =>" + platform.getPort());
            createChannel(platform.getPort(), platform.getMsgScale());
        }
    }

    /**
     * 建立通道：端口
     *
     * @param
     */
    public void createChannel(int port, String scale) {
        ChannelFuture f = null;
        try {
            //串行组
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            if ("10".equals(scale)) {
                                socketChannel.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
                                socketChannel.pipeline().addLast("encoder", new StringDecoder(Charset.forName("UTF-8")));
                            }
                            if ("16".equals(scale)) {
                                socketChannel.pipeline().addLast("decoder", new HexDecoder());
                                socketChannel.pipeline().addLast("encoder", new HexEncoder());//发送
                                //socketChannel.pipeline().addLast("encoder", new StringDecoder(Charset.forName("UTF-8")));
                            }
//                          为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                            socketChannel.pipeline().addLast(serverHandler);

                        }
                    });

            f = b.bind(port).sync();
            Channel channel = f.channel();
            //储存
            ChannelMap.put(port, channel);
            BossGroupMap.put(port, bossGroup);
            WorkerGroupMap.put(port, workerGroup);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("[TCP]Netty server[" + scale + "] on port " + port + " and ready for connections...");
            } else {
                log.error("[TCP]Netty server start up Error!");
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
        EventLoopGroup workerGroup = WorkerGroupMap.get(port);
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
            WorkerGroupMap.remove(port);
        }
        EventLoopGroup bossGroup = BossGroupMap.get(port);
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
            BossGroupMap.remove(port);
        }
        log.info("[TCP]Netty server listening on port " + port + " Shutdown Success!");
    }

}
