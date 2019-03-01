package com.lunch.account.service;

import com.lunch.account.constants.Mob;
import com.lunch.account.dao.UserDao;
import com.lunch.account.web.asClient.response.MobResponse;
import com.lunch.account.web.asService.request.ForgetPwd;
import com.lunch.account.web.asService.request.PhoneLogin;
import com.lunch.account.web.asService.request.Register;
import com.lunch.redis.support.HashRedisService;
import com.lunch.redis.support.StringRedisService;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.entity.*;
import com.lunch.account.web.asService.request.ChangePwdUser;
import com.lunch.support.http.HttpUtils;
import com.lunch.support.service.BaseService;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.MD5Utils;
import com.lunch.support.tool.UidUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService {
    // 加盐
    private static final String TOKEN_YAN = "tokenMRf1$789787aadfjkds//*-+'[]jfeu;384785*^*&%^%$%";
    private static final String SESSION_YAN = "sessionMRf1$789787aadfjkds//*-+'[]jfeu;384785*^*&%^%$%";
    //图形验证码，十分钟过期
    private static final int IMAGE_CODE_INTERVAL = 10 * 60;

    private static final String REDIS_HKEY_ACCOUNT = "account";
    private static final String REDIS_HKEY_TOKEN = "session:";

    @Autowired
    private UserDao userDao;

    @Autowired
    private HashRedisService<AccessUser> userHashRedisService;

    @Autowired
    private StringRedisService<String> imageCodeRedisService;

    public List<AccessUser> getUserByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    //接口注册用户，这里需要做传入的user是否能够注册的判断
    public AccessUser registerUser(Register user) {
        List<AccessUser> users = userDao.selectByUsername(user.getUsername());
        if (users.size() != 0) {
            LogNewUtils.warn("username " + user.getUsername() + " is exist");
            return null;
        }

        if (!checkCode(user.getUsername(), user.getCode() + "")) {
            LogNewUtils.error("code verify error!");
            return null;
        }

        AccessUser accessUser = registerAccessUser(user);
        //把生成的账号存入db中
        long id = userDao.registerByUser(accessUser);

        LogNewUtils.info("username " + accessUser.getUsername() + " save db and id is " + id);
        //把注册的账号放入redis中
        saveUser2Redis(accessUser);
        return id != 0 ? accessUser : null;
    }

    //登录
    public AccessUser login(BaseUser baseUser) {
        AccessUser user = userHashRedisService.get(baseUser.getUsername(), REDIS_HKEY_ACCOUNT);
        if (user == null) {
            List<AccessUser> oldUsers = userDao.selectByUsername(baseUser.getUsername());
            if (oldUsers == null || oldUsers.size() == 0) {
                LogNewUtils.info("can't find username in db");
                return null;
            }

            for (AccessUser oldUser : oldUsers) {
                if (baseUser.getPassword().equals(oldUser.getPassword())) {
                    //数据库中账号对过了，正确，那么把这个账号放入redis
                    saveUser2Redis(oldUser);
                    //更新数据的登录时间
                    return updateLoginTime(oldUser.getId()) ? oldUser : null;
                }
            }
            LogNewUtils.error("found by username but password is not the same :" + baseUser.getUsername());
            return null;
        }

        LogNewUtils.info(String.format("find account %s in redis , toString is :%s", baseUser.getUsername(), user.toString()));
        //进行redis验证
        if (baseUser.getPassword().equals(user.getPassword())) {
            LogNewUtils.info("redis finish account :" + baseUser.getUsername());
            saveUser2Redis(user, true);
            //更新数据库的登录时间
            return updateLoginTime(user.getId()) ? user : null;
        }

        //db和redis都验证失败了
        LogNewUtils.error("db and redis don't have username:" + baseUser.getUsername());
        return null;
    }

    public AccessUser phoneLogin(PhoneLogin phoneLogin) {
        if (!checkCode(phoneLogin.getUsername(), phoneLogin.getCode() + "")) {
            LogNewUtils.error("code verify error!");
            return null;
        }

        AccessUser user = userHashRedisService.get(phoneLogin.getUsername());
        if (user == null) {
            List<AccessUser> oldUsers = userDao.selectByUsername(phoneLogin.getUsername());
            if (oldUsers == null || oldUsers.size() == 0) {
                LogNewUtils.info("can't find username in db");
                return null;
            }
            saveUser2Redis(oldUsers.get(0));
            updateLoginTime(oldUsers.get(0).getId());
            return oldUsers.get(0);
        }

        saveUser2Redis(user, true);
        updateLoginTime(user.getId());
        return user;
    }

    public AccessUser quickLogin(String username, String session) {
        AccessUser user = userHashRedisService.get(username, getSessionHKey(session));
        if (user == null) {
            return null;
        }

        if (user.hasSession() && user.getSession().compare(session)) {
            updateLoginTime(user.getId());
            setToken(user);
            saveUser2Redis(user);
            return user;
        }
        deleteUserInRedis(user, getSessionHKey(session));
        return null;
    }

    public ServiceEntity<AccessUser> forgetPwd(ForgetPwd forgetPwd) {
        if (!checkCode(forgetPwd.getUsername(), forgetPwd.getCode() + "")) {
            LogNewUtils.error("code verify error!");
            return new ServiceEntity<>(Code.CODE_ERROR, S.CODE_ERROR);
        }

        List<AccessUser> dbUsers = userDao.selectByUsername(forgetPwd.getUsername());
        if (dbUsers != null && !dbUsers.isEmpty()) {
            LogNewUtils.info(String.format("find user %s in db", forgetPwd.getUsername()));
            userDao.updatePwdById(dbUsers.get(0).getId(), forgetPwd.getNewPwd());

            AccessUser user = userHashRedisService.get(forgetPwd.getUsername(), REDIS_HKEY_ACCOUNT);
            LogNewUtils.info("forget password ,find in redis :" + user);
            if (user != null) {
                user.setPassword(forgetPwd.getNewPwd());
                saveUser2Redis(user, true);
            }
            return ServiceEntity.obtain(user);
        }

        LogNewUtils.info(String.format("can not find user %s in db", forgetPwd.getUsername()));
        return new ServiceEntity<>(Code.CHANGE_FAILED, S.CHANGE_FAILED);
    }

    public AccessUser verify(String username, String session) {
        AccessUser user = userHashRedisService.get(username, getSessionHKey(session));
        if (user == null) {
            return null;
        }

        if (user.hasSession() && user.getSession().compare(session)) {
            return user;
        }
        deleteUserInRedis(user, getSessionHKey(session));
        return null;
    }

    public AccessUser saveAvatar(AccessUser user) {
        userDao.saveAvatar(user.getId(), user.getAvatar());
        saveUser2Redis(user);
        return user;
    }

//    public AccessUser token(String token) {
//        AccessUser user = userRedisService.get(token);
//        if (user == null) {
//            return null;
//        }
//
//        if (user.hasSession() && user.getToken().compare(token)) {
//            updateLoginTime(user.getId());
//            return user;
//        }
//        return null;
//    }

    //把图片验证码根据设备id存起来
    public void saveImageCodeByDeviceId(String deviceId, String code) {
        LogNewUtils.info("deviceId " + deviceId + " product code is " + code);
        imageCodeRedisService.put(deviceId, code, IMAGE_CODE_INTERVAL);
    }

    public ServiceEntity<AccessUser> check(String username) {
        List<AccessUser> users = userDao.selectByUsername(username);
        return new ServiceEntity<>(users.get(0));
    }

    //修改密码
    public AccessUser changePwd(ChangePwdUser user) {
        if (!checkImageCode(user.getDeviceId(), user.getCode())) {
            LogNewUtils.error("change password failed,because check image code failed");
            return null;
        }

        List<AccessUser> oldUsers = userDao.selectByUsername(user.getUsername());
        if (oldUsers == null || oldUsers.size() == 0) {
            LogNewUtils.error("db do not find user by username " + user.getUsername());
            return null;
        }

        for (AccessUser oldUser : oldUsers) {
            if (oldUser.getPassword().equals(user.getPassword())) {
                //找到来密码
                LogNewUtils.info("find user by username " + user.getUsername());
                userDao.updatePwdById(oldUser.getId(), user.getNewPwd());
                LogNewUtils.info("update db is " + oldUser.getId());
                AccessUser newUser = userDao.selectById(oldUser.getId());
                LogNewUtils.info("newUser:" + newUser.toString());
                saveUser2Redis(newUser);
                return newUser;
            }
        }

        LogNewUtils.error("db find account by username " + user.getUsername() + " but password is invalid");
        return null;
    }


    //生成uid等
    private AccessUser registerAccessUser(BaseUser user) {
        AccessUser accessUser = new AccessUser(user);
        String uid = UidUitls.generateSequenceNo(user.getUsername());
        accessUser.setUid(uid);
        setToken(accessUser);
        setSession(accessUser);
        return accessUser;
    }

    private void setToken(AccessUser user) {
        long time = System.currentTimeMillis();
        Token token = new Token(MD5Utils.toMD5(time + TOKEN_YAN + user.getUid()));
        user.setToken(token);
    }

    private void setSession(AccessUser user) {
        long time = System.currentTimeMillis();
        Session session = new Session(MD5Utils.toMD5(time + SESSION_YAN + user.getUid()));
        user.setSession(session);
    }

    //更新登录时间
    private boolean updateLoginTime(long id) {
        if (id <= 0) {
            LogNewUtils.error("update login time,but id <= 0:" + id);
            return false;
        }
        LogNewUtils.info("will be updated id is " + id);
        userDao.updateLoginTime(id, new Date());
        return true;
    }

    //检查图形验证码
    private boolean checkImageCode(String deviceId, String code) {
        String oldCode = imageCodeRedisService.get(deviceId);
        LogNewUtils.info("deviceId " + deviceId + " redis code is " + oldCode + " and user code is " + code);
        boolean compare = code.equals(oldCode);
        if (compare) {
            LogNewUtils.info("remove deviceId " + deviceId + " code " + oldCode);
            imageCodeRedisService.remove(deviceId);
        }
        return compare;
    }

    /**
     * 单纯的以username为key存在redis中，如果redis中包含这个key
     *
     * @param user   存入的对象
     * @param update 是否强行更新session和token，如果为false，则考虑对象中是否有session和token，
     *               没有就添加保证对象中一定有session和token
     */
    private void saveUser2Redis(AccessUser user, boolean update) {
        //删除原来的session
        if (user.hasSession()) {
            AccessUser sessionUser = userHashRedisService.get(user.getUsername(), getSessionHKey(user.getSession().getResult()));
            if (sessionUser != null) {
                userHashRedisService.remove(user.getUsername(), getSessionHKey(user.getSession().getResult()));
            }
        }
        //设置新的session和token
        if (update) {
            setToken(user);
            setSession(user);
        } else {
            if (!user.hasSession()) {
                setSession(user);
            }
            if (!user.hasToken()) {
                setToken(user);
            }
        }
        //存入hkey为account的对象
        userHashRedisService.put(user.getUsername(), REDIS_HKEY_ACCOUNT, user);
        //存入hkey为session:{session}的对象
        userHashRedisService.put(user.getUsername(), getSessionHKey(user.getSession().getResult()), user);
    }

    public void saveUser2Redis(AccessUser user) {
        saveUser2Redis(user, false);
    }

    private void deleteUserInRedis(AccessUser user, String hKey) {
        if (user == null) {
            LogNewUtils.info("There is no bean in redis");
            return;
        }

        userHashRedisService.remove(user.getUsername(), hKey);
    }

    private void deleteUserInRedis(AccessUser user) {
        if (user == null) {
            LogNewUtils.info("There is no bean in redis");
            return;
        }
        userHashRedisService.remove(user.getUsername());
    }

    private boolean checkCode(String phone, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appkey", Mob.APPKEY);
        params.put("phone", phone);
        params.put("zone", Mob.ZONE);
        params.put("code", code);
        MobResponse response = HttpUtils.syncRequestJson(HttpUtils.Method.POST, Mob.URL, params, MobResponse.class);
        return response.isSuccess();
    }

    private String getSessionHKey(String hKey) {
        return REDIS_HKEY_TOKEN + hKey;
    }
}
