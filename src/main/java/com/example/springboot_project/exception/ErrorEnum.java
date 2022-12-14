package com.example.springboot_project.exception;

/**
 * 异常信息枚举类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/14 22:46
 */
public enum ErrorEnum {

    ILLEGAL_TOKEN(1001, "非法的token"),
    EXPIRED_TOKEN(1002, "token已过期"),

    USER_NOT_FOUND(2001, "没有查找到对应的用户信息"),

    TEST_ERROR(3001, "测试异常");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
