package com.qdoner.datarecept.server.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HexEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        //将16进制字符串转为数组
        byteBuf.writeBytes(hexString2Bytes(s));
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */

    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }
}
