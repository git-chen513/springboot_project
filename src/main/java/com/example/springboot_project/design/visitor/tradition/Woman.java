package com.example.springboot_project.design.visitor.tradition;

/**
 * 女人类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/3 22:43
 */
public class Woman implements Person {
    @Override
    public void score(String score) {
        if ("成功".equals(score)) {
            System.out.println("女人给的评分是成功");
        } else if ("失败".equals(score)) {
            System.out.println("女人给的评分是失败");
        }
    }
}
