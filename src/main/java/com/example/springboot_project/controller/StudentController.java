package com.example.springboot_project.controller;

import com.example.springboot_project.model.Student;
import com.example.springboot_project.service.IStudentService;
import com.example.springboot_project.support.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/8 00:31
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @RequestMapping("/query_id")
    public ResponseData queryById(@RequestParam Integer id) {
        return studentService.queryById(id);
    }
}
