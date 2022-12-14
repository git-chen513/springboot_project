package com.example.springboot_project.support;

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

    public static <T> ResponseData<T> success(T data) {
        return new ResponseData(CODE_200, STATUS_OK, data);
    }

//    public static ResponseData success(Object data) {
//        return new ResponseData(CODE_200, STATUS_OK, data);
//    }

    public static ResponseData success(Object data, String message) {
        if (StringUtils.isNotEmpty(message)) {
            return new ResponseData(CODE_200, message, STATUS_OK, data);
        }
        return new ResponseData(CODE_200, STATUS_OK, data);
    }

    public static <T> ResponseData<T> failure(int code, String message) {
        return new ResponseData<T>(code, message, STATUS_ERROR);
    }
}
