package com.lunch.account.controller;

import com.lunch.account.feignService.IFastDFSService;
import com.lunch.support.entity.AccessUser;
import com.lunch.account.service.UserService;
import com.lunch.support.controller.BaseController;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController extends BaseController {

    @Value("${com.lunch.resources}")
    private String res;

    @Autowired
    private UserService userService;

    @Autowired
    private IFastDFSService fastDFSService;

    @GetMapping("/hello")
    public String hello() {
        return "你是个煞笔吗，自己礼物也不选，你在家不闲的慌吗，你知道鸡吃多了容易变成煞笔吗，网瘾少女！！！！！！";
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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(MultipartFile file) {
        return res + fastDFSService.upload(file);
    }
}
