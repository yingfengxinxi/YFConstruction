package com.kakarote.core.workflow;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.BusinessTypeEnum;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.wf.WfService;
import com.kakarote.core.feign.wf.entity.WfVO;
import com.kakarote.core.workflow.util.WfUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;


@Slf4j
@Component
@Aspect
public class WfAps implements Ordered {

    @Autowired
    private WfService wfService;

    public WfAps() {
        System.out.println("WfAop");
    }


    @Around("@annotation(WfAnnotation)")
    public Object arround(ProceedingJoinPoint joinPoint) {
        Object result = Result.error(-1,"未知错误");
        try {
            WfAnnotation wfAnnotation = getAnnotationLog(joinPoint);
            JSONObject json = JSONObject.parseObject(JSONUtil.toJsonStr(joinPoint.getArgs()[wfAnnotation.index()]));
            JSONObject wfVOJson = json.getJSONObject("wfVO");
            WfVO wfVO = new WfVO();
            BeanUtil.copyProperties(wfVOJson, wfVO);
            //是否前置
            String idName = BusinessTypeEnum.parse(wfVO.getBusinessType()).getIdName();
            if (null != json.get(idName) && wfAnnotation.check()) {
                //主键不为空 判断状态
                Object id = json.get(idName);
                wfVO.setObjectId(id);
                Result<Map<String, String>> sult = wfService.getObject(wfVO);
                if (sult.hasSuccess()) {
                    Map<String, String> map = sult.getData();
                    String checkStatus = String.valueOf(map.get("checkStatus"));
                    if (checkStatus != null && !checkStatus.equals("null")) {
                        if(!WfUtil.canEdit(Integer.parseInt(checkStatus))){
                            return Result.error(-1, "业务已审批，不能修改");
                        }
                    }
                } else {
                    return sult;
                }
            }
            // 执行方法
            result = joinPoint.proceed();
            //获取执行后的参数
            json = JSONObject.parseObject(JSONUtil.toJsonStr(joinPoint.getArgs()[wfAnnotation.index()]));
            //提交审批
            wfVO.setObjectId(json.get(idName));
            wfService.submit(wfVO);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        //throw new RuntimeException();
        return result;
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private WfAnnotation getAnnotationLog(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(WfAnnotation.class);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -999;
    }
}
