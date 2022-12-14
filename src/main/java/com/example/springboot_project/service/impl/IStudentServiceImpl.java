package com.example.springboot_project.service.impl;

import com.example.springboot_project.dao.IStudentDao;
import com.example.springboot_project.exception.ErrorEnum;
import com.example.springboot_project.exception.ServiceException;
import com.example.springboot_project.model.Student;
import com.example.springboot_project.service.IStudentService;
import com.example.springboot_project.support.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/8 01:01
 */
@Service
public class IStudentServiceImpl implements IStudentService {

    private static Logger logger = LoggerFactory.getLogger(IStudentServiceImpl.class);

    @Autowired
    private IStudentDao studentDao;

    @Override
    public ResponseData queryById(Integer id) {
        logger.info("查询id为：{} 的学生信息", id);
        Student student = studentDao.queryById(id);
        if (student == null) {
            return ResponseData.failure(ErrorEnum.USER_NOT_FOUND);
        }
        return ResponseData.success(student);
    }

    @Override
    public ResponseData test() {
        // 抛出自定义异常，测试异常是否会被全局捕获，以优雅的格式返回给前端
        throw new ServiceException(ErrorEnum.TEST_ERROR);
    }
}
