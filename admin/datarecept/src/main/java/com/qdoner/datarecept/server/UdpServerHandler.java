package com.qdoner.datarecept.server;

import com.qdoner.datarecept.service.IReceptService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 服务端业务处理
 *
 * @author wudw
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Autowired
    private IReceptService receptService;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        Channel channel = ctx.channel();
        int localPort = ((InetSocketAddress)channel.localAddress()).getPort();
        //
        String clientIP = packet.sender().getAddress().getHostAddress();
        //todo msg 为接收到的消息
        Object response = receptService.receptMsg(packet.content().toString(StandardCharsets.UTF_8),
                clientIP,localPort);
        if (response != null) ctx.write(response);
        ctx.flush();
    }
}
