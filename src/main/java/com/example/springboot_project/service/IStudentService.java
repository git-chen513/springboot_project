package com.example.springboot_project.service;

import com.example.springboot_project.model.Student;

/**
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/8 01:00
 */
public interface IStudentService {

    Student queryById(Integer id);
}
