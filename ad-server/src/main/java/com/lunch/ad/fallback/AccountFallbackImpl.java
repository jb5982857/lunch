package com.lunch.ad.fallback;

import com.lunch.ad.feignService.IAccountService;
import com.lunch.support.constants.Code;
import com.lunch.support.result.BaseResult;
import com.lunch.support.result.VerifyResult;
import org.springframework.stereotype.Component;

@Component
public class AccountFallbackImpl implements IAccountService {
    @Override
    public BaseResult verify(String session) {
        return new BaseResult(Code.ACCOUNT_DOWN, "");
    }
}
