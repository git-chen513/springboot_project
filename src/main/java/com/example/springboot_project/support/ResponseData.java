package com.example.springboot_project.support;

import com.example.springboot_project.exception.ErrorEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * api返回格式约定
 */
public class ResponseData<T> {

    /**
     * 返回码
     **/
    private int code;

    /**
     * 返回消息
     **/
    private String message;

    /**
     * 消息状态
     */
    private boolean success;

    /**
     * 返回数据
     **/
    private T data;

    private static final int CODE_200 = 200;
    private static final int CODE_400 = 400;
    /**
     * 消息状态为成功
     */
    private static final boolean STATUS_OK = true;
    /**
     * 消息状态为失败
     */
    protected static final boolean STATUS_ERROR = false;

    public ResponseData() {
        this(CODE_200, STATUS_OK);
    }

    public ResponseData(int code, String message, boolean success, T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public ResponseData(int code, boolean success) {
        this.code = code;
        this.success = success;
    }

    public ResponseData(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public ResponseData(int code, boolean success, T data) {
        this.code = code;
        this.success = success;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResponseData success() {
        return new ResponseData(CODE_200, "操作成功", STATUS_OK);
    }

    /**
     * 按理来说，所有泛型方法都应该加上<T>来声明为泛型方法，但一般来讲更多的情况是泛型方法位于被声明为泛型的类里，
     * 所以无需再声明一遍。但static属于类方法，所以需要单独加上<T>用来声明其为泛型方法
     * 即普通方法不需要加上<T>，静态方法需要加上<T>
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseData<T> success(T data) {
        return new ResponseData(CODE_200, STATUS_OK, data);
    }

    public static <T> ResponseData<T> success(T data, String message) {
        if (StringUtils.isNotEmpty(message)) {
            return new ResponseData(CODE_200, message, STATUS_OK, data);
        }
        return new ResponseData(CODE_200, STATUS_OK, data);
    }

    public static ResponseData failure(int code, String message) {
        return new ResponseData(code, message, STATUS_ERROR);
    }

    public static ResponseData failure(ErrorEnum errorEnum) {
        return new ResponseData(errorEnum.getCode(), errorEnum.getMessage(), STATUS_ERROR);
    }
}
