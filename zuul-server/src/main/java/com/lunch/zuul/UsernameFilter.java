package com.lunch.zuul;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.LogUtils;
import com.lunch.support.tool.StringUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UsernameFilter extends ZuulFilter {
    public static final String PARAM_ERROR = new BaseResult(Code.PARAMS_ERROR, StringUtils.string2Unicode(S.ERROR_PARAMS)).toJson();

    /**
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        LogNewUtils.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        String username = request.getParameter("username");
        if (username == null) {
            LogNewUtils.info("no username");
            return null;
        }
        if (!StringUtils.checkUsername(username)) {
            LogNewUtils.info("username is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            try {
                ctx.getResponse().getWriter().write(PARAM_ERROR);
            } catch (IOException e) {
                LogNewUtils.printException("username params error!", e);
            }

            return null;
        }
        LogNewUtils.info("ok");
        return null;
    }
}
