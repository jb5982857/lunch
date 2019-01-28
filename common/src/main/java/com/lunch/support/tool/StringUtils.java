package com.lunch.support.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class StringUtils {

    //账号的正则表达式，8-20位的数字和字母的组合
    private static final String USERNAME_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";

    //检查账号的有效性
    public static boolean checkUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public static boolean isEmpty(String msg) {
        return null == msg || "".equals(msg);
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LogNewUtils.printException(String.format("str %s encode error", str), e);
        }
        return str;
    }

    public static String toUTF8(String str) {
        if (isEmpty(str)) {
            return str;
        }
        try {
            return new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogNewUtils.printException(String.format("str %s to utf-8 error", str), e);
        }
        return str;
    }

    /**
     * 字符串转换unicode
     * @param string
     * @return
     */
    public static String string2Unicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u").append(Integer.toHexString(c));
        }

        return unicode.toString();
    }

}
