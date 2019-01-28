package com.lunch.zuul;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.IPUtils;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ZuulInterceptor implements HandlerInterceptor {
    public static final String IP_ERROR = new BaseResult(Code.IP_ERROR, StringUtils.string2Unicode(S.IP_ERROR)).toJson();

    @Autowired
    private IPService ipService;

    //拦截器MyInterceptor------->3、请求结束之后被调用，主要用于清理工作。
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    //拦截器MyInterceptor------->2、请求之后调用，在视图渲染之前，也就是Controller方法调用之后
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    //拦截器MyInterceptor------->1、请求之前调用，也就是Controller方法调用之前。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String clientIp = IPUtils.getRealIP(request);
        LogNewUtils.info("clientIp " + clientIp + " url " + request.getRequestURI());
        if (ipService.check(clientIp)) {
            LogNewUtils.info("clientIp " + clientIp + " is in bl");
            response.getOutputStream().print(IP_ERROR);
            return false;
        }
        return true;//返回true则继续向下执行，返回false则取消当前请求
    }
}
