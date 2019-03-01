package com.lunch.support.constants;

public class S {
    //成功的描述
    public static final String SUCCESS = "成功";
    //返回给客户端的参数有误的desc
    public static final String ERROR_PARAMS = "参数有误";
    //服务器内部错误
    public static final String INNER_ERROR = "网络错误";
    //注册失败
    public static final String REGISTER_FAILED = "用户名已存在";
    //登录失败
    public static final String LOGIN_FAILED = "登录失败";
    //修改密码失败
    public static final String CHANGE_FAILED = "验证码错误或无该账号";
    //小登失败
    public static final String VERIFY_FAILED = "登录状态过期";
    //账户无效
    public static final String USERNAME_INVALID = "账号无效";
    //地点有误
    public static final String ERROR_PLACE = "地点有误，请检查后重新输入";
    //该账号没有查询到任何地点
    public static final String QUERY_PLACE_EMPTY = "该账号还没有添加该类型地点";
    //session过期
    public static final String SESSION_OUT = "登录状态过期";
    //ip被加入黑名单
    public static final String IP_ERROR = "请求次数过多";
    //上传图片有误
    public static final String AVATAR_ERROR = "上传图片有误";
    //验证码失败
    public static final String CODE_ERROR = "验证码错误，请检查后重试";
}
