package com.lunch.place.feignService;

import com.lunch.place.fallback.AccountFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-account", fallback = AccountFallbackImpl.class)
public interface IAccountService {
    @RequestMapping(value = "/iUser/check", method = RequestMethod.GET)
    long checkUsername(@RequestParam("username") String username);
}
