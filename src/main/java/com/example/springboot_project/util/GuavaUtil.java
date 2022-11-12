package com.example.springboot_project.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * guava工具类
 *
 * 参考文档：
 * https://zhuanlan.zhihu.com/p/371997195
 * https://zhuanlan.zhihu.com/p/360589986
 *
 * @version 2.0.0
 * @author canjiechen
 * @date 2022/11/12 13:53
 */
public class GuavaUtil {
    public static void main(String[] args) {
        testMultiSet();
    }

    /*** 
     * Joiner
     * 把集合（或数组或可变参数）通过指定的分隔符连接成字符串
     * 可以对空值做特殊处理，例如跳过不拼接或者替换为指定值
     * 不需要对集合的第一个元素或最后一个元素做特殊处理
     *
     */
    public static void testJoiner() {
        List<Integer> list = Arrays.asList(1,2,3);

        Joiner joiner = Joiner.on(",");
        String s = joiner.join(list);
        System.out.println(s);

        // 也可以通过链式调用
        String res = Joiner.on(",").join(list);
        System.out.println(res);

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(null);
        // 如果待处理的集合中存在null值，需要使用skipNulls处理，跳过null值，否则会报NPE
        String s1 = Joiner.on(",").skipNulls().join(list1);
        // 可以使用useForNull替代集合中的null值
        String s2 = Joiner.on(",").useForNull("这是null的替代值").join(list1);
        System.out.println(s1); // 1
        System.out.println(s2); // 1,这是null的替代值
    }

    /***
     * splitter
     * 通过指定的分隔符把字符串转换为集合
     *
     * @return
     */
    public static void testSplitter() {
        String s = "a,b,c,,  d  ,\"\"";
        Splitter splitter = Splitter.on(",")
                // 过滤掉空白字符串（不包括""）
                .omitEmptyStrings()
                // 去除每个元素的前后空格
                .trimResults();
        Iterable<String> iterable = splitter.split(s);
        System.out.println(iterable); // [a, b, c, d, ""]

        // 转换成list集合
        List<String> list = splitter.splitToList(s);
        System.out.println(list); // [a, b, c, d, ""]
    }

    /***
     * 和Lists类似的还有Sets、Maps
     * 用于创建集合、操作集合非常方便
     *
     * @return
     */
    public void testLists() {
        // 传统方式创建一个List集合
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        // 使用Lists快速创建一个集合
        ArrayList<Integer> list2 = Lists.newArrayList(1, 2, 3);

        // 传统方式对list集合做切割，例如每两个元素作为一组，使用subList太麻烦
        // 使用Lists.partition非常方便
        List<List<Integer>> partition = Lists.partition(list2, 2);
        System.out.println(partition); // [[1, 2], [3]]

        List<String> list = Lists.newArrayList("a", "ab", "abc");
        System.out.println(list); // [a, ab, abc]
        List<Integer> transform = Lists.transform(list, s -> s.length());
        System.out.println(transform); // [1, 2, 3]
    }

    /***
     * Maps
     * 使用HashMap，为了避免HashMap扩容带来的性能开销（每次扩容都会rehash，性能损耗较大），
     * 如果提前知道存放的元素个数，在创建HashMap集合的时候应该指定集合的容量
     *
     * @return
     */
    public static void testMaps() {
        // 传统方式，由于HashMap负载因子的存在，实际上当集合元素到达12个的时候，就会发生扩容（16 * 0.75)
        // 所以使用传统方式指定集合元素容量时，我们需考虑负载因子并提前计算好，否则还是有可能产生扩容
        // 例如，希望集合元素存放12个元素，那么需要new HashMap<>(16) （12 / 0.75）
        Map<String, Integer> map1 = new HashMap<>(16);

        // 使用Maps.newHashMapWithExpectedSize，不需要我们做这一步计算，方法底层已经帮我们实现好了
        // 我们希望集合容量是多少，在创建对象的时候直接指定多少即可
        HashMap<String, Integer> map2 = Maps.newHashMapWithExpectedSize(16);
    }

    /***
     * Ints
     * 类似的还有Longs、Doubles、Floats
     *
     * @return
     */
    public static void testInts() {
        // 将数组转为list
        int[] array = {1, 2, 3};
        List<Integer> list = Ints.asList(array);
        System.out.println(list); // [1, 2, 3]

        double[] d = {1.0, 2.0, 3.0};
        List<Double> doubles = Doubles.asList(d);
        System.out.println(doubles); // [1.0, 2.0, 3.0]
    }

    /*** 
     * MultiSet
     * 原生jdk为我们提供的Set集合是无序不可重复的，那如果我们想要一个无序可重复的集合，
     * 就可以使用guava提供的MultiSet，使用MutilSet可以实现元素计数的需求，
     * 例如我们想统计每个元素出现的次数，就不再需要依赖于创建一个Map集合进行统计了
     *
     * @return 
     */
    public static void testMultiSet() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("a");
        multiset.add("b");
        multiset.add("c");
        multiset.add("a");
        System.out.println(multiset); // [a x 2, b, c]

        Set<String> set = multiset.elementSet();
        System.out.println(set); // [a, b, c]

        Set<Multiset.Entry<String>> entries = multiset.entrySet();
        for (Multiset.Entry<String> entry : entries) {
            System.out.println("元素：" + entry.getElement() + "，个数：" + entry.getCount());
        }
    }

    /***
     * MultiMap
     * MultiMap主要是用来替代jdk原生的类似这种value值也是一个集合的场景，
     * 例如：Map<String, List<Integer>>，使用原生jdk方式实现这种map集合会比较复杂
     *
     * @return
     */
    public static void testMultiMap() {
        // 1、原生jdk实现
        Map<String, List<Integer>> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        map.put("a", list);
        System.out.println(map); // {a=[1, 2, 3]}

        // 2、guava的api实现
        // HashMultimap的构造方法是私有的，只能通过create方法创建对象
        Multimap<String, Integer> map1 = HashMultimap.create();
        map1.put("a", 1);
        map1.put("a", 2);
        map1.put("a", 3);
        System.out.println(map); // {a=[1, 2, 3]}
        // 是否包含key=a,value=1的entry
        System.out.println(map1.containsEntry("a", 1)); // true
        // 转换为jdk原生api的map结构
        Map<String, Collection<Integer>> jdkMap = map1.asMap();
    }

    /*** 
     * 不可变集合
     * guava为我们提供了非常实用的不可变集合，例如ImmutableList、ImmutableSet、ImmutableMap
     * 可以保证我们创建的集合不被修改，如果别人修改了，程序会抛出异常；
     * 不可变集合的应用场景有：假设我们有一些值存放到缓存中，希望这部分值是只读的，别人不能修改，
     * 就可以使用这些不可变集合
     *
     * PS：应该尽量使用不可变集合，性能更佳
     *
     * @return 
     */
    public void testImmutable() {
        ImmutableList<Object> immutableList = ImmutableList.builder().add(1).build();
        // immutableList.add(1); // 执行会报错：UnsupportedOperationException

        ImmutableMap<Object, Object> immutableMap = ImmutableMap.builder().put("a", 1).build();
        // immutableMap.put("b", 2); // // 执行会报错：UnsupportedOperationException

        ImmutableSet<Object> immutableSet = ImmutableSet.builder().add(1).build();
        // immutableSet.add(2); // // 执行会报错：UnsupportedOperationException

        ImmutableMap<String, Integer> immutableMap1 = ImmutableMap.of("a", 1, "b", 2);
        System.out.println(immutableMap1); // {a=1, b=2}

        ImmutableList<Integer> immutableList1 = ImmutableList.of(1, 2);
        System.out.println(immutableList1); // [1, 2]

        // 虽然jdk有类似的api可以实现不可变集合，但是并无法完全保证是不可变的集合，例如：
        List<Integer> list = new ArrayList<>();
        list.add(1);
        List<Integer> unmodifyList = Collections.unmodifiableList(list);
        // unmodifyList.add(2); // 执行会报错：UnsupportedOperationException
        list.add(2); // 可以修改成功，集合还是被改变了
    }
}
