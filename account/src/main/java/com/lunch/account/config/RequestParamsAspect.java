package com.lunch.account.config;

import com.lunch.support.entity.BaseUser;
import com.lunch.support.exception.ParamsException;
import com.lunch.support.exception.UsernameException;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller aop拦截器，为了判断username是否正确
 */
@Aspect
@Component
public class RequestParamsAspect {

    @Pointcut(value = "execution(public * com.lunch.account.controller..*.*(..))")
    public void params() {
    }

    @Before("params()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseUser) {
                if (((BaseUser) arg).isEmpty()) {
                    throw new ParamsException("param error " + request.getRequestURL().toString());
                }
                if (!StringUtils.checkUsername(((BaseUser) arg).getUsername())) {
                    LogNewUtils.info("username " + ((BaseUser) arg).getUsername() + " is not ok");
                    throw new UsernameException("username invaild " + request.getRequestURL().toString());
                }
                break;
            }
        }
    }

}
