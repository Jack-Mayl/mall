package com.longzai.mall.exception;

/**
 * 异常枚举
 */
public enum LongZaiMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码长度不能少于8位"),
    NAME_EXISTED(10004,"不允许重名-注册失败"),
    INSERT_FAILED(10005,"插入失败，请重试"),
    WRONG_PASSWORD(10006,"账户或者密码错误"),
    SYSTEM_ERROR(20000,"系统异常");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    LongZaiMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
