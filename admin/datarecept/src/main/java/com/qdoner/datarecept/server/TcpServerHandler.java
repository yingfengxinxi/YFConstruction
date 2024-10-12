package com.qdoner.datarecept.server;

import com.qdoner.datarecept.service.IReceptService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
@ChannelHandler.Sharable
public class TcpServerHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private IReceptService receptService;

    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //ctx.channel().remoteAddress().toString()的第一位是"/".
        log.info("System Message ==> Client Channel Create Success, Client IP/Port: {}", ctx.channel().remoteAddress().toString().substring(1));
    }

    /**
     * 对每一个传入的消息都要调用；
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        int port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();
        //todo msg 为接收到的消息
        String response = receptService.receptMsg((String) msg,ctx.channel().remoteAddress().toString().substring(1), port);
        if (response != null) ctx.write(response);
        ctx.flush();
    }

    /**
     * 通知ChannelInboundHandler最后一次对channelRead()的调用时当前批量读取中的的最后一条消息。
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间，有异常抛出时会调用。
     * 关闭TCP连接。
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
