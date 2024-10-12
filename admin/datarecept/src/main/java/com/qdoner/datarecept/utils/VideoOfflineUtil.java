package com.qdoner.datarecept.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_YUVJ420P;

@Slf4j
public class VideoOfflineUtil {

    public static void main(String[] args) {
        String url = "https://open.ys7.com/v3/openlive/33011081992397348766:33010097991117116212_2_2.m3u8?expire=1738623629&id=545861945622802432&t=73f3ff7d3ca922172d872e11f744d661c83fb83f4b418983ea54bffe7630ac8c&ev=100&devProto=gb28181";
        boolean b = checkVideoOnline(url);
        System.out.println(b);
        boolean c = openMedia(url);
        System.out.println(c);
    }

    // TODO 新读流方式待测试
    public static boolean checkVideoOnline(String urlStr) {
        try {
            if (StringUtils.isEmpty(urlStr)) {
                return false;
            }
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                if (conn.getInputStream() != null) {
//                    log.info("wvp视频流可以打开");
                    return true;
                } else {
                    log.info("wvp视频流无法打开==" + urlStr);
                }
            } else {
                log.info("wvp视频流无法打开，HTTP状态码为" + conn.getResponseCode() + "==" + urlStr);
            }
            conn.getInputStream().close();
            conn.disconnect();
            return false;
        } catch (Exception e) {
            log.info("流媒体无法打开");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开流媒体，取一帧，转为YUVJ420P，再保存为jpg文件
     *
     * @param url
     * @throws IOException
     */
    public static boolean openMedia(String url) {
        log.info("正在打开流媒体 [{}]", url);
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        boolean bo = false;
        try {
            // 打开指定流媒体，进行解封装，得到解封装上下文
            AVFormatContext pFormatCtx = getFormatContext(url);

            if (null != pFormatCtx) {
                bo = true;
                avformat_close_input(pFormatCtx);
                pFormatCtx.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bo;
    }

    private static AVFormatContext getFormatContext(String url) {
        // 解封装上下文
        AVFormatContext pFormatCtx = new AVFormatContext(null);

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
