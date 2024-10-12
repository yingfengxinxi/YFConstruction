/**  
* @Title: HttpClientResult.java  
* @Package com.qdoner.lyswjw.util.WordUtil.HttpRequest  
* @Description: TODO(用一句话描述该文件做什么)  
* @author wnj58  
* @date 2020年12月1日  
* @version V1.0  
*/
package com.kakarote.build.utils;




import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author wnj58
 * @ClassName HttpClientResult
 * @Description: TODO(这里用一句话描述这个类的作用)  
 * @Date 2020年12月1日上午11:37:34
 */
public class HttpClientResult implements Serializable {
    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private Object content;

    public HttpClientResult() {

    }

    public HttpClientResult(int code) {
        this.code = code;
    }

    public HttpClientResult(Object content) {
        this.content = content;
    }

    public HttpClientResult(int code, Object content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JSONObject getContent() {
        return (JSONObject) content;
    }
    public String getStringContent() {
        return  String.valueOf(content);
    }
    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HttpClientResult [code=" + code + ", content=" + content + "]";
    }

}
