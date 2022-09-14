package com.longzai.mall.exception;

public class LongZaiMallException extends Exception {
    private final Integer code;
    private final String message;

    public LongZaiMallException(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public LongZaiMallException(LongZaiMallExceptionEnum longZaiMallExceptionEnum){
        this(longZaiMallExceptionEnum.code,longZaiMallExceptionEnum.msg);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
