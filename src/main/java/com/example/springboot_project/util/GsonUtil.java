package com.example.springboot_project.util;

import com.example.springboot_project.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gson工具类
 *
 * Gson是目前功能最强的JSON解析器，并且不需要依赖额外jar包（轻量、避免出现jar包冲突），能够直接运行在java平台，
 * 与另一款优秀的JSON解析器FastJson相比Gson的优势是可以准确顺利的转换复杂Bean，
 * 而FastJson对一些复杂Bean的转换就会出现一些问题，例如一个java对象又嵌套java对象，FastJson解析有时会报错
 *
 * Gson常用的两个方法
 *  1、toJson()：用于序列化
 *  2、fromJson()：用于反序列化
 *
 * Gson常用注解
 *  1、@SerializedName：指定json字符串中的字段名称
 *     @SerializedName("login_name")：指定json字符串中的字段名称为 login_name
 *     @SerializedName(value = "login_name", alternate = "name")：代表json字符串中是login_name就用login_name的值，如果是name就用name值
 * 2、@Expose：可以设置某个字段是否参与序列化和反序列化
 *    @Expose()：参与序列化和反序列化
 *    @Expose(serialize = false,deserialize = false)
 *
 * Json转换的场景
 *  1、将JavaBean转换为json，或将json字符串转换为JavaBean
 *  2、将List集合转换为json，或将json转换为List集合
 *  3、将Map集合转换为json，或将json转换为Map集合
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/11/11 00:42
 */
public class GsonUtil {
    public static void main(String[] args) {
        String s = "{\"name\": \"xiaoming\"}";
        System.out.println(validateJson(s));
    }

    /***
     * 场景一：将JavaBean转换为json，或将json字符串转换为JavaBean
     *
     * @return
     */
    public static void test1() {
        Person p = new Person("xiaoming", 18, false, Arrays.asList("a", "b"));
        Gson gson = new Gson();
        String s = gson.toJson(p);
        System.out.println(s);

        Person person = gson.fromJson(s, Person.class);
        System.out.println(person);
    }

    /***
     * 场景二：将List集合转换为json，或将json转换为List集合
     *
     * @return
     */
    public static void test2() {
        List<Person> list = new ArrayList<Person>();
        list.add(new Person("三笠·阿克曼", 16, false, Arrays.asList("砍巨人", "保护艾伦")));
        list.add(new Person("阿明·阿诺德", 16, true, Arrays.asList("看书", "玩海螺")));
        Gson gson = new Gson();
        String listJson = gson.toJson(list);
        System.out.println(listJson);

        System.out.println(list); // [Person{name='三笠·阿克曼', age=16, isMale=false, hobbies=[砍巨人, 保护艾伦]}, Person{name='阿明·阿诺德', age=16, isMale=true, hobbies=[看书, 玩海螺]}]
        System.out.println(list.get(0).getClass()); // class Person
        List list1 = gson.fromJson(listJson, List.class);
        System.out.println(list1); // [{name=三笠·阿克曼, age=16.0, isMale=false, hobbies=[砍巨人, 保护艾伦]}, {name=阿明·阿诺德, age=16.0, isMale=true, hobbies=[看书, 玩海螺]}]
        System.out.println(list1.get(0).getClass()); // class com.google.gson.internal.LinkedTreeMap
        List<Person> list2 = gson.fromJson(listJson, new TypeToken<List<Person>>(){}.getType());
        System.out.println(list2);
        System.out.println(list2.get(0).getClass()); // class Person
    }

    /***
     * 场景三：将Map集合转换为json，或将json转换为Map集合
     *
     * @return
     */
    public static void test3() {
        Map<String, Person> map = new HashMap<>();
        map.put("p1", new Person("利威尔·阿克曼", 35, true, Arrays.asList("砍猴儿", "打扫卫生")));
        map.put("p2", new Person("韩吉·佐耶", 33, false, Arrays.asList("研究巨人", "讲故事")));
        Gson gson = new Gson();
        String mapJson = gson.toJson(map);
        System.out.println(mapJson);

        Map<String, Person> jsonMap = gson.fromJson(mapJson, new TypeToken<Map<String, Person>>() {}.getType());
        System.out.println(jsonMap);
    }

    /***
     * 测试 @SerializedName 注解
     *
     * @return
     */
    public static void  testSerializedName() {
        // 由于name属性加上了注解：@SerializedName(value = "login_name", alternate = "name")
        // 因此不管json字符串的字段名是login_name还是name，都会被映射到Person实体类的name属性

        Gson gson = new Gson();

        String s1 = "{\"name\":\"韩吉·佐耶\",\"age\":33,\"isMale\":false,\"hobbies\":[\"研究巨人\",\"讲故事\"]}";
        Person person1 = gson.fromJson(s1, Person.class);
        System.out.println(person1);

        String s2 = "{\"login_name\":\"韩吉·佐耶\",\"age\":33,\"isMale\":false,\"hobbies\":[\"研究巨人\",\"讲故事\"]}";
        Person person2 = gson.fromJson(s2, Person.class);
        System.out.println(person2);
    }

    /***
     *  测试 @Expose 注解
     *
     * @return
     */
    public static void testExpose() {
        /**
         * 使用@Expose注解必须使用下面的方式创建Gson对象，否则注解不生效
         * 另外，加上该注解后，Gson的toJson()和fromJson()方法将会先排除掉没有被注解@Expose所标记的字段
         * 然后还会排除加上了@Expose注解，但是serialize和deserialize都被设置成false的字段
         * 当进行序列化时，会排除加上了@Expose注解，但是serialize被设置成false的字段
         * 当进行反序列化时，会排除加上了@Expose注解，但是deserialize被设置成false的字段
         */
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();

        Person person1 = new Person("韩吉·佐耶", 33, false, Arrays.asList("研究巨人", "讲故事"));
        String s1 = gson.toJson(person1);
        System.out.println(s1); // {"isMale":false,"hobbies":["研究巨人","讲故事"]}

        String s2 = "{\"name\":\"韩吉·佐耶\",\"age\":33,\"isMale\":false,\"hobbies\":[\"研究巨人\",\"讲故事\"]}";
        Person person2 = gson.fromJson(s2, Person.class);
        System.out.println(person2); // Person{name='null', age=0, isMale=false, hobbies=[研究巨人, 讲故事]}
    }

    /***
     * 用于检验json字符串是否有效
     *
     * @param jsonStr
     * @return
     */
    public static boolean validateJson(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }
}
