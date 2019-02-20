package com.lunch.place.feignService;

import com.lunch.place.fallback.AccountFallbackImpl;
import com.lunch.support.result.BaseResult;
import com.lunch.support.result.VerifyResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-account", fallback = AccountFallbackImpl.class)
public interface IAccountService {
    @RequestMapping(value = "/user/verify", method = RequestMethod.GET)
    BaseResult<VerifyResult> verify(@RequestParam("session") String session);
}
