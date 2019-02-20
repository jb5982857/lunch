package com.lunch.place.config;

import com.lunch.place.feignService.IAccountService;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.exception.SessionOutException;
import com.lunch.support.result.BaseResult;
import com.lunch.support.result.VerifyResult;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "placeFilter")
public class PlaceFilter implements Filter {
    //session过期
    public static final int SESSION_OUT = -1;
    //用户系统挂了
    public static final int ACCOUNT_DOWN = -2;

    @Autowired
    private IAccountService accountService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!StringUtils.isEmpty(request.getParameter("session"))) {
            long userId = verify(request.getParameter("session"));
            if (userId == SESSION_OUT) {
                if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse) response).sendError(Code.SESSION_OUT, S.SESSION_OUT);
                }
                LogNewUtils.error("session " + request.getParameter("session") + " is xpired");
            }
            if (userId == ACCOUNT_DOWN) {
                throw new RuntimeException("account down exception!");
            }
            request.setAttribute("userId", userId);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    //-1是session过期，客户端应该回到登录界面
    //-2是网络错误，请求用户系统失败
    private long verify(String session) {
        BaseResult<VerifyResult> result = accountService.verify(session);
        if (result == null) {
            return SESSION_OUT;
        }
        if (result.getCode() == Code.ACCOUNT_DOWN) {
            return ACCOUNT_DOWN;
        }
        if (result.getCode() != Code.SUCCESS) {
            return SESSION_OUT;
        }
        return result.getData().getUserId();
    }
}
