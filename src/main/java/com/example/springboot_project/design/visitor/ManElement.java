package com.example.springboot_project.design.visitor;

/**
 * 具体元素类：男人类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/1 01:24
 */
public class ManElement implements PersonElement{
    @Override
    public void accept(ActionVisitor actionVisitor) {
        // 把自身引用传递给访问者的方法，这样访问者对象就可以处理元素对象上的操作
        actionVisitor.visit(this);
    }

    public void testMan() {
        System.out.println("这是男人给的评分");
    }
}
