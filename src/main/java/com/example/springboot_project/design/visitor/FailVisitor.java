package com.example.springboot_project.design.visitor;

/**
 * 具体访问者类：评分为失败的操作类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/1 01:28
 */
public class FailVisitor implements ActionVisitor{
    @Override
    public void visit(ManElement manElement) {
        manElement.testMan();
        System.out.println("男人给的评分是失败");
    }

    @Override
    public void visit(WomanElement womanElement) {
        womanElement.testWoman();
        System.out.println("女人给的评分是失败");
    }
}
