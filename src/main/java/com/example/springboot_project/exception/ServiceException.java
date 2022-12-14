package com.example.springboot_project.exception;

/**
 * 业务异常类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/15 00:45
 */
public class ServiceException extends RuntimeException{

    private ErrorEnum errorEnum;

    public ServiceException() {}

    /***
     * 自定义枚举异常
     */
    public ServiceException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }

    /***
     * 常规异常
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /***
     * 常规异常和自定义枚举异常
     */
    public ServiceException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.getMessage(), cause);
        this.errorEnum = errorEnum;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorEnum getErrorEnum() {
        return errorEnum;
    }
}
