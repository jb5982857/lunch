package com.lunch.account.dao;

import com.lunch.support.entity.AccessUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface UserDao {
    //根据账号id去获取用户
    List<AccessUser> selectByUsername(@Param("username") String username);

    //根据用户去申请账户
    long registerByUser(AccessUser user);

    //更新数据库的登录时间
    void updateLoginTime(@Param("id") long id, @Param("date") Date date);

    //根据username来查密码
    String queryPwdByUsername(@Param("username") String username);

    //更新密码
    void updatePwdById(@Param("id") long id, @Param("newPwd") String newPwd);

    //根据id去查找用户
    AccessUser selectById(@Param("id") long id);
}
