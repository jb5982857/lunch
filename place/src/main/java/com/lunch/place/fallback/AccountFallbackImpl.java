package com.lunch.place.fallback;

import com.lunch.place.feignService.IAccountService;
import com.lunch.support.constants.Code;
import com.lunch.support.result.BaseResult;
import com.lunch.support.result.VerifyResult;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.LogUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountFallbackImpl implements IAccountService {
    @Override
    public BaseResult<VerifyResult> verify(String session) {
        return new BaseResult<VerifyResult>(Code.ACCOUNT_DOWN, "");
    }
}
