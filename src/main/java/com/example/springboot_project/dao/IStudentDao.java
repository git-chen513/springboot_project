package com.example.springboot_project.dao;

import com.example.springboot_project.model.Student;

/**
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/8 01:02
 */
public interface IStudentDao {

    Student queryById(Integer id);
}
