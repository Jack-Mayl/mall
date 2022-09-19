package com.longzai.mall.exception;

import com.longzai.mall.common.ApiRestResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 *  描述： 处理统一异常的handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        log.error("Default Exception",e);
        return ApiRestResponse.error(LongZaiMallExceptionEnum.SYSTEM_ERROR);
    }
    @ExceptionHandler(LongZaiMallException.class)
    @ResponseBody
    public Object handleLongZaiMallException(LongZaiMallException e){
        log.error("LongZaiMall Exception",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValid Exception",e);
        return handleBindingResult(e.getBindingResult());
    }
    private ApiRestResponse handleBindingResult(BindingResult result){
        // 把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String defaultMessage = objectError.getDefaultMessage();
                list.add(defaultMessage);
            }
        }
        if(list.size() == 0){
            return ApiRestResponse.error(LongZaiMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(LongZaiMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }

}
