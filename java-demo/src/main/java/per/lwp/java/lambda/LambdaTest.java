package per.lwp.java.lambda;

import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * parallelStream 和 stream 读取操作都是线程安全的
 * created by lawnp on 2018/12/17.
 */
public class LambdaTest {


    private static List list = Arrays.asList("abc", 1, 2, "abc", "名字1", 2, "ccc");
    private static List listSorted = Arrays.asList("abc", "3", "1", "2", "abb", "名字1", 2, "ccc");
    private static Set<String> dataSet = new HashSet();

    private static List<Person> persons = new ArrayList<>();
    static {
        dataSet.add("222");
        dataSet.add("234");
        dataSet.add("123");
        dataSet.add("222");
        dataSet.add("333");
        dataSet.add("名字");
        dataSet.add("333");
        // 总共 5个元素
        System.out.println(dataSet);

        persons.add(new Person(1, "张三1", 33, new Date(), 0));
        persons.add(new Person(2, "李四2", 11, new Date(), 0));
        persons.add(new Person(3, "王五3", 36, new Date(), 0));
        persons.add(new Person(4, "马六4", 22, new Date(), 1));
        persons.add(new Person(5, "马六4", 22, new Date(), 1));
    }

    @Test
    public void testForEach() {
        // parallelStream并行流, 顺序不按集合顺序输出
        list.parallelStream().forEach(item -> {
            System.out.println(item);
        });
        System.out.println("**********");
        // stream顺序流, 按集合顺序输出
        list.stream().forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void testForEachOrdered() {
//        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> numbers = Arrays.asList(1, 2, 3, 7, 8, 9, 4, 5, 6);
        System.out.println("排序后读取:  stream按List顺序读取 ********");
        numbers.stream().sorted().forEach((System.out::println));
        System.out.println("forEachOrdered 按List顺序读取 ********");
        numbers.parallelStream().forEachOrdered(System.out::println);

        System.out.println("parallelStream 乱序读取 ********");
        numbers.parallelStream().forEach(System.out::println);
        System.out.println("stream().parallel() 乱序读取 ********");
        numbers.stream().parallel().forEach(System.out::println);

        // parallelStream并行流, 顺序不按集合顺序输出 -- java.lang.String cannot be cast to java.lang.Integer
        numbers.parallelStream().forEachOrdered(item -> {
            System.out.print(item + "\t");
        });
        System.out.println("\n**********");
//        Stream.of("AAA", "BBB", "CCC").parallel()
    }

    @Test
    public void testList2Set() {
        // list转set
        Set<Object> set = (Set<Object>) list.parallelStream().collect(Collectors.toSet());
        set.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void testSet2List() {
        // set转list
        List<String> list = (List<String>) dataSet.parallelStream().collect(Collectors.toList());
        list.forEach(item -> {
            System.out.println(item);
        });

        System.out.println("*******");
        // 顺序和上面并行的顺序一致
        // set转list
        List<String> list1 = (List<String>) dataSet.stream().collect(Collectors.toList());
        list1.forEach(item -> {
            System.out.println(item);
        });
    }

    /**
     * 截取： limit(3) 保留前三个元素
     */
    @Test
    public void testLimit() {
        // list转set
        System.out.println("处理前*****");
        dataSet.parallelStream().forEach(item -> {
            System.out.println(item);
        });
        System.out.println("处理后*****");
        dataSet.parallelStream().limit(3).forEach(item -> {
            System.out.println(item);
        });
    }

    /**
     * count() 返回元素个数
     */
    @Test
    public void testCount() {
        // list转set
        System.out.println("dataSet元素总数*****");
        System.out.println(dataSet.parallelStream().count());

        // 过滤数据
        System.out.println(dataSet.parallelStream().filter((String item) -> {
            // 返回true则被保留, false数据被过滤
            return item.equals("222");
        }).count());
    }

    /**
     * filter() 过滤数据 只保留返回true的
     */
    @Test
    public void testFilter() {
        // 过滤数据
        System.out.println(dataSet.parallelStream().filter((String item) -> {
            // 返回true则被保留, false数据被过滤
            return item.equals("222");
        }).count());
    }

    /**
     * Predicate 需要返回 true 或 false
     *
     * allMatch
     * anyMatch
     * noneMatch
     */
    @Test
    public void testPredicate() {
        //Predicate<? super T> predicate
        // 过滤并打印出过滤后结果
        dataSet.parallelStream().filter((String item) -> {
            // 返回true则被保留, false数据被过滤
            return item.equals("222");
        }).forEach(item -> {
            System.out.println(item);
        });

        // 是否所有都匹配item.equals("222")
        System.out.println("是否所有都匹配item.equals(\"222\"): " + dataSet.parallelStream().allMatch((String item) -> {
            return item.equals("222");
        }));

        // 是否有任一匹配
        System.out.println("是否有任一匹配item.equals(\"222\"): " + dataSet.parallelStream().anyMatch((String item) -> {
            return item.equals("222");
        }));

        // 是否所有不匹配
        System.out.println("是否所有不匹配item.equals(\"222\"): " + dataSet.parallelStream().noneMatch((String item) -> {
            return item.equals("222");
        }));
    }

    @Test
    public void testToArray() {
        // MALE = 0
        // 将集合转化为数组, 集合的toArray方法 返回Object[]
        Object[] men = persons.toArray();
        for (int i = 0; i < men.length; i++) {
            System.out.println((Person) men[i]);
        }
        //
        System.out.println("stream().toArray方法: ***");
        Person[] men1 = persons.stream().toArray(Person[]::new);
        for (int i = 0; i < men1.length; i++) {
            System.out.println(men1[i]);
        }
        // 将过滤的集合转化为数组
        Person[] men2 = persons.stream().filter(p -> p.getGender() == 0).toArray(Person[]::new);
        for (int i = 0; i < men2.length; i++) {
            System.out.println(men2[i]);
        }

        // 将过滤的集合转化为数组
        /*Predicate p1 = (p) -> {
            return true;
        };
        Predicate p2 = (p) -> {
            return true;
        };
        Predicate p3 = p1.and(p2); 多条件*/
    }

    @Test
    public void testCollect() {
        // 集合
        persons.stream().filter(item -> item.getId() > 2).collect(Collectors.toList()).forEach(System.out::println);

        Stream<String> stringStream = Stream.of("ab", "ccc", "999");
        String concat = stringStream.collect(StringBuilder::new, StringBuilder::append,StringBuilder::append).toString();
        System.out.println(concat);
        persons.stream().filter(item -> item.getId() > 2).collect(HashSet::new, HashSet::add, HashSet::add).forEach(System.out::println);
        System.out.println("********");
        persons.stream().filter(item -> item.getId() > 2).collect(ArrayList::new, ArrayList::add, ArrayList::add).forEach(System.out::println);
    }

    @Test
    public void testReduce() {

        // 1.一个参数
        Optional<String> reduce = persons.stream().map(Person::getName).reduce(new BinaryOperator<String>() {
            @Override
            public String apply(String str1, String str2) {
                return str1 + str2;
            }
        });
        reduce.ifPresent(result -> System.out.println(result));

//        Stream<String> stringStream = Stream.of("ab", "ccc", "999");
        Stream<String> stringStream = Stream.of("ab", "ccc", "999");
        Optional<String> reduce1 = stringStream.reduce(new BinaryOperator<String>() {
            @Override
            public String apply(String str1, String str2) {
                return str1 + str2;
            }
        });
        reduce1.ifPresent(item -> System.out.println(item));

        // 2. 两个参数
        String reduce2 = persons.stream().map(Person::getName).reduce("[value]", new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                return s.concat(s2);
            }
        });
        System.out.println(reduce2);

//        Stream<Integer> intStream = Stream.of(11, 22, 33);
        List<Integer> intStream = Arrays.asList(11, 22, 33);

        // identity 起始值  第二个参数操作
        Integer reduce3 = intStream.stream().reduce(1, new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });
        System.out.println(reduce3);

        // 3. 三个参数
        Integer reduce4 = persons.stream().reduce(10, (sum, b) -> sum + b.getAge(), Integer::sum);
        System.out.println(reduce4);

        Integer reduce5 = intStream.stream().reduce(10, (sum, b) -> sum + b, Integer::sum);
        System.out.println(reduce5);
    }
}
