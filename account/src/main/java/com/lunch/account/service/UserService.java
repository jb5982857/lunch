package com.lunch.account.service;

import com.lunch.account.dao.UserDao;
import com.lunch.redis.support.StringRedisService;
import com.lunch.support.entity.BaseUser;
import com.lunch.support.entity.AccessUser;
import com.lunch.support.entity.Session;
import com.lunch.support.entity.Token;
import com.lunch.account.entity.ChangePwdUser;
import com.lunch.support.service.BaseService;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.MD5Utils;
import com.lunch.support.tool.UidUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService {
    // 加盐
    private static final String TOKEN_YAN = "tokenMRf1$789787aadfjkds//*-+'[]jfeu;384785*^*&%^%$%";
    private static final String SESSION_YAN = "sessionMRf1$789787aadfjkds//*-+'[]jfeu;384785*^*&%^%$%";
    //图形验证码，十分钟过期
    private static final int IMAGE_CODE_INTERVAL = 10 * 60;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisService<AccessUser> userRedisService;

    @Autowired
    private StringRedisService<String> imageCodeRedisService;

    public List<AccessUser> getUserByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    //接口注册用户，这里需要做传入的user是否能够注册的判断
    public AccessUser registerUser(BaseUser user) {
        List<AccessUser> users = userDao.selectByUsername(user.getUsername());
        if (users.size() != 0) {
            LogNewUtils.warn("username " + user.getUsername() + " is exist");
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
        AccessUser user = userRedisService.get(baseUser.getUsername());
        if (user == null) {
            List<AccessUser> oldUsers = userDao.selectByUsername(baseUser.getUsername());
            if (oldUsers == null || oldUsers.size() == 0) {
                LogNewUtils.info("can't find username in db");
                return null;
            }

            for (AccessUser oldUser : oldUsers) {
                if (MD5Utils.toMD5(baseUser.getPassword()).equals(oldUser.getPassword())) {
                    //数据库中账号对过了，正确，那么把这个账号放入redis
                    saveUser2Redis(oldUser);
                    //更新数据的登录时间
                    return updateLoginTime(oldUser.getId()) ? oldUser : null;
                }
            }
            LogNewUtils.error("found by username but password is not the same :" + baseUser.getUsername());
            return null;
        }

        //进行redis验证
        if (MD5Utils.toMD5(baseUser.getPassword()).equals(user.getPassword())) {
            LogNewUtils.info("redis finish account :" + baseUser.getUsername());
            setToken(user);
            setSession(user);
            saveUser2Redis(user);
            //更新数据库的登录时间
            return updateLoginTime(user.getId()) ? user : null;
        }

        //db和redis都验证失败了
        LogNewUtils.error("db and redis don't have username:" + baseUser.getUsername());
        return null;
    }

    public AccessUser quickLogin(String session) {
        AccessUser user = userRedisService.get(session);
        if (user == null) {
            return null;
        }

        if (user.hasSession() && user.getSession().compare(session)) {
            updateLoginTime(user.getId());
            setToken(user);
            saveUser2Redis(user);
            return user;
        }
        deleteUserInRedis(user);
        return null;
    }

    public AccessUser verify(String session) {
        AccessUser user = userRedisService.get(session);
        if (user == null) {
            return null;
        }

        if (user.hasSession() && user.getSession().compare(session)) {
            return user;
        }
        deleteUserInRedis(user);
        return null;
    }

    public AccessUser token(String token) {
        AccessUser user = userRedisService.get(token);
        if (user == null) {
            return null;
        }

        if (user.hasSession() && user.getToken().compare(token)) {
            updateLoginTime(user.getId());
            return user;
        }
        return null;
    }

    //把图片验证码根据设备id存起来
    public void saveImageCodeByDeviceId(String deviceId, String code) {
        LogNewUtils.info("deviceId " + deviceId + " product code is " + code);
        imageCodeRedisService.put(deviceId, code, IMAGE_CODE_INTERVAL);
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

        String md5Pwd = MD5Utils.toMD5(user.getPassword());
        for (AccessUser oldUser : oldUsers) {
            if (oldUser.getPassword().equals(md5Pwd)) {
                //找到来密码
                LogNewUtils.info("find user by username " + user.getUsername());
                userDao.updatePwdById(oldUser.getId(), MD5Utils.toMD5(user.getNewPwd()));
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

    private void saveUser2Redis(AccessUser user) {
        AccessUser redisUser = userRedisService.get(user.getUsername());
        deleteUserInRedis(redisUser);
        if (!user.hasSession()) {
            setSession(user);
        }
        if (!user.hasToken()) {
            setToken(user);
        }
        LogNewUtils.info("saveUser to redis ,user " + user.toString());
        userRedisService.put(user.getSession().getResult(), user);
        userRedisService.put(user.getToken().getResult(), user);
        userRedisService.put(user.getUsername(), user);
    }

    private void deleteUserInRedis(AccessUser user) {
        if (user == null) {
            LogNewUtils.info("There is no bean in redis");
            return;
        }

        userRedisService.remove(user.getUsername());
        if (user.hasSession()) {
            userRedisService.remove(user.getSession().getResult());
        }

        if (user.hasToken()) {
            userRedisService.remove(user.getToken().getResult());
        }
    }
}
