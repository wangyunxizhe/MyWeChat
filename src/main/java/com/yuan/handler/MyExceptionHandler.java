package com.yuan.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangy on 2018/11/26.
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String, Object> exceptionHandler(HttpServletRequest request, Exception e) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", false);//因为任何方法中被捕获到了异常，所以都会默认返回false
        modelMap.put("errMsg", e.getMessage());
        return modelMap;
    }
}
