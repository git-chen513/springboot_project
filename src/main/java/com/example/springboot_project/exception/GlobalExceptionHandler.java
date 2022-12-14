package com.example.springboot_project.exception;

import com.example.springboot_project.support.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常统一拦截处理，以优雅的格式返回前端
 *
 * 注意：只会拦截controller接口抛出的异常，spring security过滤器抛出的异常不会被拦截处理
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/15 01:28
 */

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务自定义异常
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseData serviceExceptionHandler(ServiceException e) {
        logger.warn("业务自定义异常", e);
        if (null != e.getErrorEnum()) {
            return ResponseData.failure(e.getErrorEnum().getCode(), e.getMessage());
        } else {
            return ResponseData.failure(400, e.getMessage());
        }
    }

    /**
     * 其他异常
     */
    @ExceptionHandler({ Exception.class })
    public String exception(Exception e) {
        logger.warn("其他未知异常", e);
        return ResponseData.failure(400, e.getMessage()).toString();
    }
}
