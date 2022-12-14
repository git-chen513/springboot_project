package com.example.springboot_project.service;

import com.example.springboot_project.support.ResponseData;

/**
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/8 01:00
 */
public interface IStudentService {

    ResponseData queryById(Integer id);

    ResponseData test();
}
