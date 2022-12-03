package com.example.springboot_project.design.visitor;

/**
 * 访问者接口
 *
 * 包含多个重载方法，根据参数类型进行区分，有多少个具体元素类就有多少个重载的方法
 *
 * 备注：具体的访问者类也可以不需要实现抽象访问者的全部方法，进行按需实现。
 * 那么需要把抽象的访问者定义为抽象类，不要定义为接口。
 * 或者定义为接口，再通过一个抽象类实现该接口，并给每个方法定义为默认实现，
 * 再让具体的访问者类继承于该抽象类，再进行按需实现重写某个方法即可。
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/1 01:23
 */
public interface ActionVisitor {

    void visit(ManElement manElement);

    void visit(WomanElement womanElement);

}
