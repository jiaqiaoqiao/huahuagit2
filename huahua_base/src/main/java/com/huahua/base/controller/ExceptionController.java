package com.huahua.base.controller;

import huahua.common.Result;
import huahua.common.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
//ControllerAdvice 如果异常的话 每一次请求都会处理
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result errorHandle(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }

}
