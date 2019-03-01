package com.lunch.zuul;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.exception.IPException;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.IPUtils;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class IpFilter extends ZuulFilter {
    public static final String IP_ERROR = new BaseResult(Code.IP_ERROR, S.IP_ERROR).toJson();

    @Autowired
    private IPService ipService;

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
        String clientIp = IPUtils.getRealIP(request);
        LogNewUtils.info("clientIp " + clientIp + " url " + request.getRequestURI());
        if (ipService.check(clientIp)) {
            LogNewUtils.info("clientIp " + clientIp + " is in bl");
            //不对其进行路由
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.getResponse().setHeader("Content-type", "text/html;charset=UTF-8");
            ctx.getResponse().setCharacterEncoding("UTF-8");
            try {
                ctx.getResponse().getWriter().write(IP_ERROR);
            } catch (IOException e) {
                LogNewUtils.printException("clientIp exception", e);
            }
//            throw new IPException("");
        }
        LogNewUtils.info("ok");
        return null;
    }
}
