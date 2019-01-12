package com.lunch.account.controller;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.entity.BaseUser;
import com.lunch.support.entity.AccessUser;
import com.lunch.account.entity.ChangePwdUser;
import com.lunch.account.result.AccountResult;
import com.lunch.support.controller.BaseController;
import com.lunch.support.result.BaseResult;
import com.lunch.account.service.UserService;
import com.lunch.support.tool.AuthImageUtils;
import com.lunch.support.tool.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
//添加父路径
@RequestMapping("/user")
public class AccountController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult registerAccount(BaseUser baseUser) {
        LogUtils.info("registerAccount :" + baseUser.toString());

        AccessUser user = userService.registerUser(baseUser);
        if (user != null) {
            return new AccountResult(user.getSession(), user.getUsername(), user.getUid(), "1");
        }
        return new BaseResult(Code.REGISTER_FAILED, S.REGISTER_FAILED);
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult login(BaseUser baseUser) {
        LogUtils.info("login :" + baseUser.toString());

        AccessUser user = userService.login(baseUser);
        if (user != null) {
            return new AccountResult(user.getSession(), user.getUsername(), user.getUid(), "1");
        }
        return new BaseResult(Code.LOGIN_FAILED, S.LOGIN_FAILED);
    }

    //修改密码
    @RequestMapping(value = "/change", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult changePassword(ChangePwdUser user) {
        AccessUser newUser = userService.changePwd(user);
        if (newUser != null) {
            return new AccountResult(newUser.getSession(), newUser.getUsername(), newUser.getUid(), "1");
        }

        return new BaseResult(Code.CHANGE_FAILED, S.CHANGE_FAILED);
    }

    //获取图形验证码
    @GetMapping("/image_code")
    public void getImageCode(String deviceId, HttpServletResponse response) {
        if (deviceId == null) {
            LogUtils.info("deviceId is null");
            return;
        }
        LogUtils.info("deviceId:" + deviceId);

        userService.saveImageCodeByDeviceId(deviceId, AuthImageUtils.getAuthImage(response));
    }
}
