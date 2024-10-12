package com.kakarote.build.utils;

import com.kakarote.build.wxapp.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.*;

import java.io.IOException;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_YUVJ420P;
@Slf4j
public class VideoOfflineUtil {

    private static final int DEST_FORMAT = AV_PIX_FMT_YUVJ420P;

    /**
     * 打开流媒体，取一帧，转为YUVJ420P，再保存为jpg文件
     * @param url
     * @throws IOException
     */
    public static boolean openMedia(String url) {
        log.info("正在打开流媒体 [{}]", url);
        if(StringUtils.isEmpty(url)){
            return false;
        }
        boolean bo = false;
        try {
            // 打开指定流媒体，进行解封装，得到解封装上下文
            avformat.AVFormatContext pFormatCtx = getFormatContext(url);

            if (null!=pFormatCtx) {
                bo = true;
                avformat_close_input(pFormatCtx);
                pFormatCtx.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  bo;
    }

    private static avformat.AVFormatContext getFormatContext(String url) {
        // 解封装上下文
        avformat.AVFormatContext pFormatCtx = new avformat.AVFormatContext(null);

        // 打开流媒体
        if (avformat_open_input(pFormatCtx, url, null, null) != 0) {
            log.error("打开媒体失败");
            return null;
        }

        // 读取流媒体数据，以获得流的信息
        if (avformat_find_stream_info(pFormatCtx, (PointerPointer<Pointer>) null) < 0) {
            log.error("获得媒体流信息失败");
            return null;
        }

        return pFormatCtx;
    }
}
