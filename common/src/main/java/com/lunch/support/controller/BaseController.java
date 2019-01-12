package com.lunch.support.controller;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;

/**
 * controller的基础类
 * 封装了一些公共资源
 */
public class BaseController {
    public static final BaseResult OK = new BaseResult(Code.SUCCESS, S.SUCCESS);
}
