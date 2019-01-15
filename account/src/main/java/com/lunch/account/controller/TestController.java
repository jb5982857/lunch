package com.lunch.account.controller;

import com.lunch.support.entity.AccessUser;
import com.lunch.account.service.UserService;
import com.lunch.support.controller.BaseController;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "你好啊，傻逼许二娃111111222333333！";
    }

    @GetMapping("/user")
    public List<AccessUser> getUser(String username) {
        LogNewUtils.info("getUser and username is " + username);
        return userService.getUserByUsername(username);
    }

    @GetMapping("/error")
    public String error(String username) {
        return "error1";
    }
}
