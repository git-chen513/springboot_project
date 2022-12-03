package com.example.springboot_project.design.visitor;

/**
 * 元素类接口
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/1 01:20
 */
public interface PersonElement {

    void accept(ActionVisitor actionVisitor);
}
