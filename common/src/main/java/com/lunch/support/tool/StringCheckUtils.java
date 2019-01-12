package com.lunch.support.tool;

public class StringCheckUtils {

    //账号的正则表达式，8-20位的数字和字母的组合
    private static final String USERNAME_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";

    //检查账号的有效性
    public static boolean checkUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public static boolean isEmpty(String msg) {
        return null == msg || "".equals(msg);
    }
}
