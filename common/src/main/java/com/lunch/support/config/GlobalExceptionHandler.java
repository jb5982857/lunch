package com.lunch.support.config;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.exception.ParamsException;
import com.lunch.support.exception.UsernameException;
import com.lunch.support.result.BaseResult;
import com.lunch.support.tool.LogUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    public static final BaseResult PARAM_ERROR = new BaseResult(Code.PARAMS_ERROR, S.ERROR_PARAMS);
    public static final BaseResult USERNAME_INVALID = new BaseResult(Code.PARAMS_ERROR, S.USERNAME_INVALID);
    public static final BaseResult INNER_ERROR = new BaseResult(Code.INNER_ERROR, S.INNER_ERROR);

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public BaseResult deal(Exception ex) {
        if (ex instanceof ParamsException) {
            LogUtils.printException("doThrow params error!", ex);
            return PARAM_ERROR;
        }

        if (ex instanceof UsernameException) {
            LogUtils.printException("doThrow username error!", ex);
            return USERNAME_INVALID;
        }
        LogUtils.printException("server inner error!", ex);
        return INNER_ERROR;
    }
}
