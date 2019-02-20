package com.lunch.account.controller;

import com.lunch.account.feignService.IFastDFSService;
import com.lunch.account.web.asService.request.ForgetPwd;
import com.lunch.account.web.asService.request.PhoneLogin;
import com.lunch.account.web.asService.request.Register;
import com.lunch.account.web.asService.response.TokenResult;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.entity.BaseUser;
import com.lunch.support.entity.AccessUser;
import com.lunch.account.web.asService.request.ChangePwdUser;
import com.lunch.account.web.asService.response.AccountResult;
import com.lunch.support.controller.BaseController;
import com.lunch.support.result.BaseResult;
import com.lunch.account.service.UserService;
import com.lunch.support.result.VerifyResult;
import com.lunch.support.tool.AuthImageUtils;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private IFastDFSService fastDFSService;

    @Value("${com.lunch.resources}")
    private String res;

    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult registerAccount(Register register) {
        LogNewUtils.info("registerAccount :" + register.toString());

        AccessUser user = userService.registerUser(register);
        if (user != null) {
            return BaseResult.obtain(new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1", user.getAvatar()));
        }
        return new BaseResult(Code.REGISTER_FAILED, S.REGISTER_FAILED);
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult login(BaseUser baseUser) {
        LogNewUtils.info("login :" + baseUser.toString());

        AccessUser user = userService.login(baseUser);
        if (user != null) {
            return BaseResult.obtain(new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1", user.getAvatar()));
        }
        return new BaseResult(Code.LOGIN_FAILED, S.LOGIN_FAILED);
    }

    @RequestMapping(value = "/phone_login", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult phoneLogin(PhoneLogin login) {
        LogNewUtils.info("phone login :" + login.toString());
        AccessUser user = userService.phoneLogin(login);
        if (user != null) {
            return BaseResult.obtain(new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1", user.getAvatar()));
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
            return BaseResult.obtain(new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1", user.getAvatar()));
        }
        return new BaseResult(Code.VERIFY_FAILED, S.VERIFY_FAILED);
    }

    @RequestMapping(value = "/forget_password", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult forgetPwd(ForgetPwd forgetPwd) {
        LogNewUtils.info("forget_password :" + forgetPwd.toString());
        AccessUser user = userService.forgetPwd(forgetPwd);
        if (user != null) {
            return BaseResult.obtain(new AccountResult(user.getSession().getResult(), user.getUsername(), user.getUid(), user.getToken().getResult(), "1", user.getAvatar()));
        }
        return new BaseResult(Code.CHANGE_FAILED, S.CHANGE_FAILED);
    }

    @RequestMapping(value = "/send/avatar", method = RequestMethod.POST)
    public BaseResult sendAvatar(String session, MultipartFile file) {
        if (StringUtils.isEmpty(session)) {
            return PARAM_ERROR;
        }
        LogNewUtils.info("send avatar ,session " + session + "  avatar  " + file.getOriginalFilename());

        AccessUser user = userService.verify(session);
        if (user == null) {
            return new BaseResult(Code.VERIFY_FAILED, S.VERIFY_FAILED);
        }

//        //删除老的图片
//        if (user.getAvatar() != null) {
//            fastDFSService.delete(user.getAvatar());
//        }

        String path = fastDFSService.upload(file);
        if (StringUtils.isEmpty(path)) {
            return new BaseResult(Code.AVATAR_ERROR, S.AVATAR_ERROR);
        }

        LogNewUtils.info("send avatar path " + res + path);
        user.setAvatar(res + path);
        AccessUser savedUser = userService.saveAvatar(user);
        return BaseResult.obtain(new AccountResult(savedUser.getSession().getResult(), savedUser.getUsername(), savedUser.getUid(), savedUser.getToken().getResult(), "1", savedUser.getAvatar()));
    }

    @RequestMapping(value = "/verify", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult verify(String session) {
        LogNewUtils.info("verify :" + session);
        if (StringUtils.isEmpty(session)) {
            return PARAM_ERROR;
        }

        AccessUser user = userService.verify(session);
        if (user != null) {
            return BaseResult.obtain(new VerifyResult(user.getId()));
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
            return BaseResult.obtain(new TokenResult(user.getUid()));
        }
        return new BaseResult(Code.VERIFY_FAILED, S.VERIFY_FAILED);
    }

    //修改密码
    @RequestMapping(value = "/change", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResult changePassword(ChangePwdUser user) {
        AccessUser newUser = userService.changePwd(user);
        if (newUser != null) {
            return BaseResult.obtain(new AccountResult(newUser.getSession().getResult(), newUser.getUsername(), newUser.getUid(), newUser.getToken().getResult(), "1", newUser.getAvatar()));
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
