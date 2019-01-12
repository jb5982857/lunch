package com.lunch.account.service;

import com.lunch.support.config.StringRedisService;
import com.lunch.account.dao.UserDao;
import com.lunch.support.entity.BaseUser;
import com.lunch.support.entity.AccessUser;
import com.lunch.support.entity.Token;
import com.lunch.account.entity.ChangePwdUser;
import com.lunch.support.service.BaseService;
import com.lunch.support.tool.MD5Utils;
import com.lunch.support.tool.UidUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService {
    // 加盐
    private static final String YAN = "testMRf1$789787aadfjkds//*-+'[]jfeu;384785*^*&%^%$%";
    // token过期时间，单位秒
    private static final int ACCOUNT_INTERVAL = 7 * 24 * 60 * 60;
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
            logger.info("username " + user.getUsername() + " is exist");
            return null;
        }
        AccessUser accessUser = registerAccessUser(user);
        //把生成的账号存入db中
        long id = userDao.registerByUser(accessUser);

        logger.info("username " + accessUser.getUsername() + " save db and id is " + id);
        //把注册的账号放入redis中
        userRedisService.put(accessUser.getUsername(), accessUser, ACCOUNT_INTERVAL);
        return id != 0 ? accessUser : null;
    }

    //生成uid等
    private AccessUser registerAccessUser(BaseUser user) {
        AccessUser accessUser = new AccessUser(user);
        String uid = UidUitls.generateSequenceNo(user.getUsername());
        accessUser.setUid(uid);
        long time = System.currentTimeMillis();
        Token token = new Token(MD5Utils.toMD5(time + YAN + uid), time);
        accessUser.setToken(token);
        return accessUser;
    }

    //登录
    public AccessUser login(BaseUser baseUser) {
        AccessUser user = userRedisService.get(baseUser.getUsername());
        if (user == null) {
            List<AccessUser> oldUsers = userDao.selectByUsername(baseUser.getUsername());
            if (oldUsers == null || oldUsers.size() == 0) {
                logger.info("can't find username in db");
                return null;
            }

            for (AccessUser oldUser : oldUsers) {
                if (MD5Utils.toMD5(baseUser.getPassword()).equals(oldUser.getPassword())) {
                    //数据库中账号对过了，正确，那么把这个账号放入redis
                    userRedisService.put(baseUser.getUsername(), oldUser, ACCOUNT_INTERVAL);
                    //更新数据的登录时间
                    return updateLoginTime(oldUser.getId()) ? oldUser : null;
                }
            }
            logger.info("found by username but password is not the same :" + baseUser.getUsername());
            return null;
        }

        //进行redis验证
        if (MD5Utils.toMD5(baseUser.getPassword()).equals(user.getPassword())) {
            logger.info("redis finish account :" + baseUser.getUsername());
            //更新数据库的登录时间
            return updateLoginTime(user.getId()) ? user : null;
        }

        //db和redis都验证失败了
        logger.info("db and redis don't have username:" + baseUser.getUsername());
        return null;
    }

    //更新登录时间
    private boolean updateLoginTime(long id) {
        if (id <= 0) {
            logger.info("update login time,but id <= 0:" + id);
            return false;
        }
        logger.info("will be updated id is " + id);
        userDao.updateLoginTime(id, new Date());
        return true;
    }

    //把图片验证码根据设备id存起来
    public void saveImageCodeByDeviceId(String deviceId, String code) {
        logger.info("deviceId " + deviceId + " product code is " + code);
        imageCodeRedisService.put(deviceId, code, IMAGE_CODE_INTERVAL);
    }

    //修改密码
    public AccessUser changePwd(ChangePwdUser user) {
        if (!checkImageCode(user.getDeviceId(), user.getCode())) {
            logger.info("change password failed,because check image code failed");
            return null;
        }

        List<AccessUser> oldUsers = userDao.selectByUsername(user.getUsername());
        if (oldUsers == null || oldUsers.size() == 0) {
            logger.info("db do not find user by username " + user.getUsername());
            return null;
        }

        String md5Pwd = MD5Utils.toMD5(user.getPassword());
        for (AccessUser oldUser : oldUsers) {
            if (oldUser.getPassword().equals(md5Pwd)) {
                //找到来密码
                logger.info("find user by username " + user.getUsername());
                userDao.updatePwdById(oldUser.getId(), MD5Utils.toMD5(user.getNewPwd()));
                logger.info("update db is " + oldUser.getId());
                AccessUser newUser = userDao.selectById(oldUser.getId());
                logger.info("newUser:" + newUser.toString());
                userRedisService.put(newUser.getUsername(), newUser, ACCOUNT_INTERVAL);
                return newUser;
            }
        }

        logger.info("db find account by username " + user.getUsername() + " but password is invalid");
        return null;
    }

    //检查图形验证码
    private boolean checkImageCode(String deviceId, String code) {
        String oldCode = imageCodeRedisService.get(deviceId);
        logger.info("deviceId " + deviceId + " redis code is " + oldCode + " and user code is " + code);
        boolean compare = code.equals(oldCode);
        if (compare) {
            logger.info("remove deviceId " + deviceId + " code " + oldCode);
            imageCodeRedisService.remove(deviceId);
        }
        return compare;
    }
}
