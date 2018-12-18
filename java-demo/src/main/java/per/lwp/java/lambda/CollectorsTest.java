package per.lwp.java.lambda;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created by lawnp on 2018/12/18.
 */
public class CollectorsTest {

    private static List<Person> persons = new ArrayList<>();
    static {
        // 总共 5个元素
        persons.add(new Person(1, "张三1", 33, new Date(), 0));
        persons.add(new Person(2, "李四2", 11, new Date(), 0));
        persons.add(new Person(3, "王五3", 36, new Date(), 0));
        persons.add(new Person(4, "马六4", 22, new Date(), 1));
        persons.add(new Person(5, "马六4", 22, new Date(), 1));
    }

    //    Collectors.summingInt(); // 和        Collectors.toSet()
//        Collectors.toList()
//        Collectors.toMap()
//        Collectors.toCollection();
//        Collectors.toConcurrentMap()
//
//        Collectors.groupingBy()
//        Collectors.groupingByConcurrent()
//        Collectors.averagingDouble() //平均数
//        Collectors.summarizingDouble()
//        Collectors.counting() // 统计条数
//        Collectors.maxBy() // 最大
//        Collectors.minBy()  // 最小
//        Collectors.joining()  // join
//        Collectors.reducing()
//        Collectors.collectingAndThen()
//        Collectors.mapping()
//        Collectors.partitioningBy()

    @Test
    public void testToList() {
        System.out.println(persons.stream().collect(Collectors.toList()));
        // filter返回boolean值即可
        //        System.out.println(persons.stream().filter((Person item) -> item.getId() > 2).collect(Collectors.toList()));
        System.out.println(persons.stream().filter(item -> item.getId() > 2).collect(Collectors.toList()));
    }

    @Test
    public void testToMap() {
        // 转HashMap
        // Duplicate key Person{id=4, name='马六4', age=22, gender=1, birthday=Tue Dec 18 20:53:21 CST 2018} 有重复的值转map异常
//        System.out.println(persons.stream().collect(Collectors.toMap(Person::getName, Person::getId)));
        // key不能重复,key有重复的值抛出异常Duplicate key
        System.out.println(persons.stream().collect(Collectors.toMap(Person::getId, Person::getName)));
        System.out.println(persons.stream().collect(Collectors.toMap(Person::getId, p -> p)));

        // 转ConcurrentMap
        System.out.println("转ConcurrentMap");
        ConcurrentMap<Integer, String> collect = persons.stream().collect(Collectors.toConcurrentMap(Person::getId, Person::getName));
        System.out.println(collect);
    }

    @Test
    public void testGroupingBy() {
        // 按name分组map 所得为HashMap
        Map<String, List<Person>> collecMap = persons.stream().collect(Collectors.groupingBy(Person::getName));
        System.out.println(collecMap);

        List<String> items = Arrays.asList("apple", "banana", "apple", "orange", "banana");
        Collector<Object, ?, Map<Object, Long>> objectMapCollector = Collectors.groupingBy(Function.identity(), Collectors.counting());
        Map<Object, Long> collect = items.stream().collect(objectMapCollector);
        System.out.println(collect);
    }
    @Test
    public void testGroupingByConcurrent() {
        // 分组name分组map  ConcurrentMap
        ConcurrentMap<String, List<Person>> collectConcurrentMap = persons.stream().collect(Collectors.groupingByConcurrent(Person::getName));
        System.out.println(collectConcurrentMap);

//        ConcurrentMap<Object, List<Person>> collectConcurrentMap1 = persons.stream().collect(Collectors.groupingByConcurrent(Function.identity()));
        // 对象 Function.identity() = Person实例
        ConcurrentMap<Person, List<Person>> collectConcurrentMap1 = persons.stream().collect(Collectors.groupingByConcurrent(Function.identity()));
        System.out.println(collectConcurrentMap1);

        System.out.println("**********实例自定义Map");
        // <String, Double> String和key对应类型
        ConcurrentSkipListMap<String, Double> collect = persons.stream().collect(Collectors.groupingByConcurrent(Person::getName,
                ConcurrentSkipListMap::new, Collectors.averagingDouble(Person::getAge)));
        Optional.of(collect.getClass()).ifPresent(System.out::println);
        // 输出 System.out.println(collect)
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    @Test
    public void testSumming() {
        // 取得所有数值总和
        System.out.println("取得所有数值总和***********");
        System.out.println("summingInt: " + persons.stream().collect(Collectors.summingInt(item -> item.getAge())));
        System.out.println("summingDouble: " +  persons.stream().collect(Collectors.summingDouble(item -> item.getAge())));
        System.out.println("summingLong: " +  persons.stream().collect(Collectors.summingLong(item -> item.getAge())));
    }

    @Test
    public void testSummarizing() {
        System.out.println("取得所有统计信息***********");
        // 取得所有统计信息 如: IntSummaryStatistics{count=4, sum=102, min=11, average=25.500000, max=36}
        System.out.println("summarizingInt: " +  persons.stream().collect(Collectors.summarizingInt(item -> item.getAge())));
        System.out.println("summarizingDouble: " +  persons.stream().collect(Collectors.summarizingDouble(item -> item.getAge())));
        System.out.println("summarizingLong: " +  persons.stream().collect(Collectors.summarizingLong(item -> item.getAge())));
    }

    /**
     * 取得年龄平均值***********
     * averagingInt: 24.8
     * averagingDouble: 24.8
     * averagingLong: 24.8
     */
    @Test
    public void testAveraging() {
        System.out.println("取得年龄平均值***********");
        System.out.println("averagingInt: " +  persons.stream().collect(Collectors.averagingInt(item -> item.getAge())));
        System.out.println("averagingDouble: " +  persons.stream().collect(Collectors.averagingDouble(item -> item.getAge())));
        System.out.println("averagingLong: " +  persons.stream().collect(Collectors.averagingLong(item -> item.getAge())));
    }

    @Test
    public void testCounting() {
        System.out.println("取得记录总条数***********");
        System.out.println("counting: " +  persons.stream().collect(Collectors.counting()));

        // Collectors.counting()返回Long
        Map<String, Long> collectMap = persons.stream().collect(Collectors.groupingBy(Person::getName, Collectors.counting()));
        System.out.println(collectMap);
    }

    @Test
    public void testMaxByAndMinBy() {
        System.out.println("取得记录总条数***********");
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
//                return o1.hashCode() - o2.hashCode();
                return o1.getId() - o2.getId();
            }
        };

        // 获取最大值 按comparator比较
        System.out.println("maxBy: " +  persons.stream().collect(Collectors.maxBy(comparator)));
        // 获取最大值
        System.out.println("maxBy: " +  persons.stream().collect(Collectors.maxBy((o1, o2) -> o1.getId() - o2.getId())));
        // 获取最小值
        System.out.println("minBy: " +  persons.stream().collect(Collectors.minBy((o1, o2) -> {
            return o1.getId() - o2.getId();
        })));
    }

    /**
     * joining加入字符串,前后增加字符串
     */
    @Test
    public void testJoining() {

        System.out.println("testJoining");
        // 拼接字符串
        Optional.of(persons.stream().map(Person::getName).collect(Collectors.joining()))
                .ifPresent(System.out::println);

        // 指定分隔符(delimiter)拼接字符串
        Optional.of(persons.stream().map(Person::getName).collect(Collectors.joining("，")))
                .ifPresent(System.out::println);

        // 指定分隔符(delimiter)拼接字符串 , 增加前缀和后缀（前后加字符串）
        // testJoiningWithDelimiterAndPrefixAndSuffix
        Optional.of(persons.stream().map(Person::getName).collect(Collectors.joining("，", "***前面的字符串***", "***后面的字符串***")))
                .ifPresent(System.out::println);

        System.out.println(persons.stream().map(Person::getName).collect(Collectors.joining()));
    }

    @Test
    public void testReducing() {

        System.out.println("testReducing");

        // 1. 一个参数
        System.out.println(persons.stream().map(Person::getName).reduce(new BinaryOperator<String>() {
            @Override
            public String apply(String str1, String str2) {
                return str1 + str2;
            }
        }));

        System.out.println(persons.stream().map(Person::getName).reduce((String str1, String str2) -> str1 + str2));

        // 2. 两个参数
        Stream<String> s = Stream.of("test", "t1", "t2", "teeeee", "aaaa", "taaa");
        // 以下结果将会是：　[value]testt1t2teeeeeaaaataaa 也可以使用Lambda语法：System.out.println(s.reduce("[value]", (s1, s2) -> s1.concat(s2)));
        System.out.println(s.reduce("[value]", new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                return s.concat(s2);
            }
        }));

        // java.lang.IllegalStateException: stream has already been operated upon or closed
        List<String> strList = Arrays.asList("test", "t1", "t2", "teeeee", "aaaa", "taaa");
        System.out.println(strList.stream().reduce("[value111]", new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                return s.concat(s2);
            }
        }));

        // 3. 三个参数, 第一个参数identity为起始值, 第二accumulator  ，第三combiner
        // U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)
        int sumOfAge = persons.stream()
                .reduce(10, (sum, b) -> sum + b.getAge(), Integer::sum);
        System.out.println(sumOfAge);

    }

    @Test
    public void testCollectingAndThen() {

        System.out.println("testCollectingAndThen");

//        List<String> people = people.stream().collect(collectingAndThen(toList(), Collections::unmodifiableList));
        // 转化为一个不可变集合
        persons.stream().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)).forEach(System.out::println);
        List<Person> collect = persons.stream().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        System.out.println(collect);

        //
        //members.stream().collect(Collectors.collectingAndThen(
        // Collectors.toCollection(()->newTreeSet<>(Comparator.comparing(UploadMemberListReqDTO::getMobile))),
        // ArrayList::new))
//        System.out.println(persons.stream().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));

    }

    @Test
    public void testMapping() {

        System.out.println("testMapping");
//        List<String> people = people.stream().collect(collectingAndThen(toList(), Collections::unmodifiableList));
        // 转化为一个不可变集合
        List<String> collect1 = persons.stream().collect(Collectors.mapping(Person::getName, Collectors.toList()));
        System.out.println(collect1);
    }

    @Test
    public void testPartitioningBy() {

        System.out.println("testPartitioningBy");
//        List<String> people = people.stream().collect(collectingAndThen(toList(), Collections::unmodifiableList));
        // 函数的返回值只能将数据分为两组也就是ture和false两组数据。
        // fasle为key  一个 true为key
        Map<Boolean, List<Person>> collect = persons.stream().collect(Collectors.partitioningBy(item -> item.getId() > 2));
        System.out.println(collect);

        // fasle为key   一个 true为key  true为空数组[] , 没有值为空数组[]
        Map<Boolean, List<Person>> collect1 = persons.stream().collect(Collectors.partitioningBy(item -> item.getId() > 10));
        System.out.println(collect1);
    }


}
