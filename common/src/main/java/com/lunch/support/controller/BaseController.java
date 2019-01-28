package com.lunch.support.controller;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;

/**
 * controller的基础类
 * 封装了一些公共资源
 */
public class BaseController {
    public static final BaseResult PARAM_ERROR = new BaseResult(Code.PARAMS_ERROR, S.ERROR_PARAMS);
    public static final BaseResult INNER_ERROR = new BaseResult(Code.INNER_ERROR, S.INNER_ERROR);
    public static final BaseResult OK = new BaseResult(Code.SUCCESS, S.SUCCESS);
}
