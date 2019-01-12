package com.lunch.account.controller;

import com.lunch.account.dao.UserDao;
import com.lunch.support.config.StringRedisService;
import com.lunch.support.controller.BaseController;
import com.lunch.support.entity.AccessUser;
import com.lunch.support.tool.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/iUser")
public class InnerController extends BaseController {
    @Autowired
    private UserDao mUserDao;
    @Autowired
    private StringRedisService<AccessUser> userRedisService;

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public long checkUser(String username) {
        AccessUser redisUser = userRedisService.get(username);
        if (redisUser == null) {
            //redis中没有账号，需要从db中找
            LogUtils.info("redis do not find username " + username + " , find it in db now!");
            List<AccessUser> users = mUserDao.selectByUsername(username);
            if (users == null || users.size() == 0) {
                LogUtils.info("add place but username " + username + " do not find in db");
                return -1;
            }

            LogUtils.info("find username " + username + " by db");
            AccessUser dbUser = users.get(0);
            return dbUser.getId();
        }

        LogUtils.info("redis found username " + username);
        return redisUser.getId();
    }
}
