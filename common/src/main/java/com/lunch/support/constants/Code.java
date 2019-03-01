package com.lunch.support.constants;

public class Code {
    //成功
    public static final int SUCCESS = 0;

    //服务器内部错误
    public static final int INNER_ERROR = -502;

    //参数有误
    public static final int PARAMS_ERROR = -1;

    //注册失败
    public static final int REGISTER_FAILED = -2;

    //登录失败
    public static final int LOGIN_FAILED = -3;

    //验证失败
    public static final int VERIFY_FAILED = -4;

    //修改密码失败
    public static final int CHANGE_FAILED = -5;

    //添加地点失败
    public static final int ADD_PLACE_FAILED = -6;

    //更改地点状态失败
    public static final int CHANGE_PLACE_STATE_FAILED = -7;

    //查询地点无
    public static final int QUERY_PLACE_NULL = -8;

    //session过期
    public static final int SESSION_OUT = -9;

    //ip 被加入黑名单
    public static final int IP_ERROR = -10;

    //头像文件保存出错
    public static final int AVATAR_ERROR = -11;

    //验证码失败
    public static final int CODE_ERROR = -12;

    //rpc交互时服务器内部错误
    public static final int BASE_SERVER = -10000;

    //account服务挂了
    public static final int ACCOUNT_DOWN = BASE_SERVER - 1;
}
