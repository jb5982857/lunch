package com.lunch.account.controller;

import com.lunch.account.result.TokenResult;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.entity.BaseUser;
import com.lunch.support.entity.AccessUser;
import com.lunch.account.entity.ChangePwdUser;
import com.lunch.account.result.AccountResult;
import com.lunch.support.controller.BaseController;
import com.lunch.support.result.BaseResult;
import com.lunch.account.service.UserService;
import com.lunch.support.result.VerifyResult;
import com.lunch.support.tool.AuthImageUtils;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@RestController
//添加父路径
@RequestMapping("/user")
public class AccountController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult registerAccount(BaseUser baseUser) {
        LogNewUtils.info("registerAccount :" + baseUser.toString());

        AccessUser user = userService.registerUser(baseUser);
        if (user != null) {
            return new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1");
        }
        return new BaseResult(Code.REGISTER_FAILED, S.REGISTER_FAILED);
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult login(BaseUser baseUser) {
        LogNewUtils.info("login :" + baseUser.toString());

        AccessUser user = userService.login(baseUser);
        if (user != null) {
            return new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1");
        }
        return new BaseResult(Code.LOGIN_FAILED, S.LOGIN_FAILED);
    }

    @RequestMapping(value = "/quick_login", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult quickLogin(String session) {
        LogNewUtils.info("quick_login :" + session);
        if (StringUtils.isEmpty(session)) {
            return PARAM_ERROR;
        }

        AccessUser user = userService.quickLogin(session);
        if (user != null) {
            return new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1");
        }
        return new BaseResult(Code.VERIFY_FAILED, S.VERIFY_FAILED);
    }

    @RequestMapping(value = "/verify", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult verify(String session) {
        LogNewUtils.info("verify :" + session);
        if (StringUtils.isEmpty(session)) {
            return PARAM_ERROR;
        }

        AccessUser user = userService.verify(session);
        if (user != null) {
            return new VerifyResult(user.getId());
        }
        return new BaseResult(Code.VERIFY_FAILED, S.VERIFY_FAILED);
    }

    @RequestMapping(value = "/token", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult token(String token) {
        LogNewUtils.info("token :" + token);
        if (StringUtils.isEmpty(token)) {
            return PARAM_ERROR;
        }

        AccessUser user = userService.token(token);
        if (user != null) {
            return new TokenResult(user.getUid());
        }
        return new BaseResult(Code.VERIFY_FAILED, S.VERIFY_FAILED);
    }

    //修改密码
    @RequestMapping(value = "/change", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult changePassword(ChangePwdUser user) {
        AccessUser newUser = userService.changePwd(user);
        if (newUser != null) {
            return new AccountResult(newUser.getSession().getResult(), newUser.getUsername(), newUser.getUid(), newUser.getToken().getResult(), "1");
        }

        return new BaseResult(Code.CHANGE_FAILED, S.CHANGE_FAILED);
    }

    //获取图形验证码
    @GetMapping("/image_code")
    public void getImageCode(String deviceId, HttpServletResponse resp) {
        if (deviceId == null) {
            LogNewUtils.info("deviceId is null");
            return;
        }
        LogNewUtils.info("deviceId:" + deviceId);
        Map<String, Object> result = AuthImageUtils.getAuthImage();

        String code = (String) result.get(AuthImageUtils.CODE);
        userService.saveImageCodeByDeviceId(deviceId, code);

        BufferedImage buffImg = (BufferedImage) result.get(AuthImageUtils.IMAGE);
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        ServletOutputStream sos = null;
        try {
            // 将图像输出到Servlet输出流中。
            sos = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", sos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
