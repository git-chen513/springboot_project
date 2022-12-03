package com.example.springboot_project.design.visitor;

/**
 * 测试类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/1 01:30
 */
public class Test {

    public static void main(String[] args) {
        ManElement manElement = new ManElement();
        // 传入的是成功的访问者，那么是进行成功的评分
        SuccessVisitor successVisitor = new SuccessVisitor();
        manElement.accept(successVisitor);
        // 传入的是失败的访问者，那么是进行失败的评分
        FailVisitor failVisitor = new FailVisitor();
        manElement.accept(failVisitor);
    }
}
