package com.example.springboot_project.service.impl;

import com.example.springboot_project.dao.IStudentDao;
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
        return ResponseData.success(student);
    }
}
