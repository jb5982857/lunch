package com.lunch.place.fallback;

import com.lunch.place.feignService.IAccountService;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.LogUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountFallbackImpl implements IAccountService {
    @Override
    public long checkUsername(String username) {
        LogNewUtils.info("account check username error!");
        return -1;
    }
}
