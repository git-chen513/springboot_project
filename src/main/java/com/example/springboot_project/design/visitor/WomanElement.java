package com.example.springboot_project.design.visitor;

/**
 * 具体元素类：女人类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/1 01:24
 */
public class WomanElement implements PersonElement{
    @Override
    public void accept(ActionVisitor actionVisitor) {
        // 把自身引用传递给访问者的方法，这样访问者对象就可以处理元素对象上的操作
        actionVisitor.visit(this);
    }

    public void testWoman() {
        System.out.println("这是女人给的评分");
    }
}
