package com.kakarote.core.common;

import com.alibaba.fastjson.JSON;
import com.kakarote.core.entity.AdminSystemLog;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.IAdminSystemLogService;
import com.kakarote.core.utils.IpUtils;
import com.kakarote.core.utils.SpringUtils;
import com.kakarote.core.utils.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Aspect
public class OperateLogService {


    public OperateLogService() {
        System.out.println("Aop");
    }

    /**
     * 切点
     */
    @Pointcut("@annotation(com.kakarote.core.common.OperateLog)")
    public void logPointCut() { }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
    {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult){
        try{
            // 获得注解
            OperateLog controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null){
                return;
            }
            AdminSystemLog adminSystemLog = new AdminSystemLog();

            UserInfo userInfo = UserUtil.getUser();

            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            adminSystemLog.setCreateUserId(userInfo.getUserId());
            adminSystemLog.setIpAddress(IpUtils.getIpAddr(attributes.getRequest()));
            adminSystemLog.setReturnValue(JSON.toJSONString(jsonResult));
            adminSystemLog.setUrl(attributes.getRequest().getRequestURI());
            if (e != null) {
                adminSystemLog.setIsSuccess(0);
                adminSystemLog.setErrMsg(getErrorMessage(e));
            }else {
                adminSystemLog.setIsSuccess(1);
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String requestMethod = attributes.getRequest().getMethod();
            adminSystemLog.setObject(className+"."+methodName);
            adminSystemLog.setRequestMethod(requestMethod);
            if ("PUT".equals(requestMethod) || "POST".equals(requestMethod))
            {
                String params = argsArrayToString(joinPoint.getArgs());
                adminSystemLog.setParameter(params);
            }
            else
            {
                Map<?, ?> paramsMap = (Map<?, ?>) attributes.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                adminSystemLog.setParameter(paramsMap.toString());
            }
            adminSystemLog.setTypes(controllerLog.types().ordinal());
            adminSystemLog.setBehavior(controllerLog.behavior().ordinal());
            adminSystemLog.setDetail(controllerLog.detail());
            SpringUtils.getBean(IAdminSystemLogService.class).save(adminSystemLog);
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    private String getErrorMessage(Exception e){
        if(null == e){
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return  sw.toString();
    }
    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, OperateLog log, AdminSystemLog operLog) throws Exception
    {


    }



    /**
     * 是否存在注解，如果存在就获取
     */
    private OperateLog getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(OperateLog.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (int i = 0; i < paramsArray.length; i++)
            {
                if (!isFilterObject(paramsArray[i]))
                {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    if(jsonObj!=null){
                        params += jsonObj.toString() + " ";
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o)
    {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
