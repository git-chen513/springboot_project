package com.example.springboot_project.util;

import com.example.springboot_project.model.Student;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * fastjson工具类
 *
 * fastjson常用的两个方法
 *  1、JSON.toJSONString()：用于序列化，将对象转为json字符串
 *  2、JSON.parseObject()：用于反序列化，将json字符串转为java对象，
 *     如果参数有传递具体类的class对象，则解析后返回对应的类对象，否则返回JSONObject
 *  3、JSON.parseArray()：用于反序列化，将json数组转为List，
 *     如果参数有传递具体类的class对象，则解析后返回对应的List类对象，否则返回JSONArray
 *
 * @JSONField注解
 *  name：序列化后的名字
 *  serialize：是否序列化该字段，默认是true
 *  deserialize：是否反序列化该字段，默认是true
 *  format：序列化后的格式，比如对于时间字段，可以指定格式"yyyy-MM-dd HH:mm:ss"
 *  ordinal：序列化的顺序
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/9 23:08
 */
public class FastJsonUtil {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    /***
     * 场景一：将JavaBean转换为json，或将json字符串转换为JavaBean
     *
     * @return
     */
    public static void test1() {
        Student student = new Student(1, 18, "xiaoming");
        String s = JSON.toJSONString(student);
        System.out.println(s);

        Student student1 = JSON.parseObject(s, Student.class);
        System.out.println(student1);
    }

    /***
     * 场景二：将List集合转换为json，或将json转换为List集合
     *
     * @return
     */
    public static void test2() {
        Student student1 = new Student(1, 18, "xiaoming");
        Student student2 = new Student(2, 20, "xiaohong");
        List<Student> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);
        String s = JSON.toJSONString(list);
        System.out.println(s);

        List<Student> list1 = JSON.parseArray(s, Student.class);
        System.out.println(list1);
    }

    /***
     * json字符串转为json对象
     *
     * @return
     */
    public static void test3() {
        String s = "{\"age\":18,\"id\":1,\"name\":\"xiaoming\"}";
        JSONObject jsonObject = JSON.parseObject(s);
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("id"));
    }

    /***
     * json数组转为json对象数组
     *
     * @return
     */
    public static void test4() {
        String s = "[{\"age\":18,\"id\":1,\"name\":\"xiaoming\"},{\"age\":20,\"id\":2,\"name\":\"xiaohong\"}]";
        JSONArray jsonArray = JSON.parseArray(s);
        System.out.println(jsonArray);
        System.out.println(jsonArray.get(0));
        System.out.println(((JSONObject)jsonArray.get(0)).get("name"));
    }

    /***
     * json对象数组转为List
     *
     * @return
     */
    public static void test5() {
        String s = "[{\"age\":18,\"id\":1,\"name\":\"xiaoming\"},{\"age\":20,\"id\":2,\"name\":\"xiaohong\"}]";
        JSONArray jsonArray = JSON.parseArray(s);
        String s1 = jsonArray.toJSONString();
        List<Student> list = JSON.parseArray(s1, Student.class);
        System.out.println(list);
    }

    /***
     * json对象转换为JavaBean
     *
     * @return
     */
    public static void test6() {
        String s = "{\"age\":18,\"id\":1,\"name\":\"xiaoming\"}";
        JSONObject jsonObject = JSON.parseObject(s);
        Student student = JSON.toJavaObject(jsonObject, Student.class);
        System.out.println(student);
    }
}
