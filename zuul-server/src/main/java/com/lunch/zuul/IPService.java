package com.lunch.zuul;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-ip")
public interface IPService {
    @RequestMapping(value = "/ip/check", method = RequestMethod.GET)
    boolean check(@RequestParam("ip") String ip);
}

