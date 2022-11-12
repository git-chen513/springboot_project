package com.example.springboot_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Person类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/11 00:43
 */

public class Person {
    @SerializedName(value = "login_name", alternate = "name")
    private String name;
    @Expose(deserialize = false, serialize = false)
    private int age;
    @Expose()
    private boolean isMale;
    @Expose()
    private List<String> hobbies;

    public Person(String name, int age, boolean isMale, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.isMale = isMale;
        this.hobbies = hobbies;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", isMale=" + isMale +
                ", hobbies=" + hobbies +
                '}';
    }
}

