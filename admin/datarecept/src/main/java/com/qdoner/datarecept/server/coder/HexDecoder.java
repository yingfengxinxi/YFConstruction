package com.qdoner.datarecept.server.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName MyDecoder
 * @Deacription TODO  netty接收对象转16进制
 * @Author LiuDaGang
 * @Date 2021/3/2 14:49
 * @Version 1.0
 **/

public class HexDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[byteBuf.readableBytes()];
        //复制内容到字节数组b
        byteBuf.readBytes(b);
        //字节数组转字符串
        String str = new String(b);

        // System.out.println(str);

        list.add(bytesToHexString(b));
    }

    public String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String toHexString1(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

}


