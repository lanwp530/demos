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

    /**
     * 常用 list转 map 分组功能
     * list转 map
     */
    @Test
    public void testMap() {

        // 1. 转list 将persons中的 name转为list  map可以做属性操作或者原值
        List<String> aa = persons.stream().map(a -> a.getName()).collect(Collectors.toList());
        aa.forEach(a->System.out.println(a));

        List<Person> personList = persons.stream().map(a -> a).collect(Collectors.toList());
        // 上面等同于List<Person> personList = persons.stream().collect(Collectors.toList());
        personList.forEach(a->System.out.println(a));

        // 2. 将集合转为MMap<String, String> key=name val=name  Collectors.toMap(key, val)
        // key不能重复,否则抛出异常java.lang.IllegalStateException: Duplicate key 马六4
//        Map<String, String> collectMap1 = persons.stream().collect(Collectors.toMap(Person::getName, Person::getName));
        // Map key的泛型类型要和后面的key一致
        Map<Integer, String> collectMap1 = persons.stream().collect(Collectors.toMap(Person::getId, Person::getName));
//        Map<Object, String> collectMap11 = persons.stream().collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println(collectMap1);

        // 3. 分组根据名称分组  Map<String, List<Person>>
        Map<String, List<Person>> collectMap = (Map<String, List<Person>>) persons.stream().collect(Collectors.groupingBy(Person::getName, Collectors.toList()));
        System.out.println(collectMap);

        // 分组根据名称分组  Map<String, List<Person>> 使用ConcurrentMap
        Map<String, List<Person>> collectConcurrentMap = (Map<String, List<Person>>) persons.stream().collect(Collectors.groupingByConcurrent(Person::getName, Collectors.toList()));
        System.out.println(collectConcurrentMap);

        // 并发map使用并行流
        Map<String, List<Person>> collectConcurrentMap1 = (Map<String, List<Person>>) persons.parallelStream().collect(Collectors.groupingByConcurrent(Person::getName, Collectors.toList()));
        System.out.println(collectConcurrentMap1);

        // 根据id分组
        Map<Integer, List<Person>> collectMap2 = (Map<Integer, List<Person>>) persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.toList()));
        System.out.println(collectMap2);

        //2.分组，并统计其中一个属性值得sum或者avg:id总和
        /*Map<String,Integer> result3=list1.stream().collect(
                Collectors.groupingBy(Student::getGroupId,Collectors.summingInt(Student::getId))
        );
        System.out.println(result3);*/

        // 5. 取得所有数据总和 14
        Integer allSum = persons.stream().collect(Collectors.summingInt(Person::getId));
        System.out.println(allSum);
        // 6. 取得分组总和  {1=1, 2=2, 3=3, 4=8}
        Map<Integer, Integer> collectMap3 = persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.summingInt(Person::getId)));
        System.out.println(collectMap3);

        // 7. 汇总统计信息{1=DoubleSummaryStatistics{count=1, sum=1.000000, min=1.000000, average=1.000000, max=1.000000}, 2=DoubleSummaryStatistics{count=1, sum=2.000000, min=2.000000, average=2.000000, max=2.000000}, 3=DoubleSummaryStatistics{count=1, sum=3.000000, min=3.000000, average=3.000000, max=3.000000}, 4=DoubleSummaryStatistics{count=2, sum=8.000000, min=4.000000, average=4.000000, max=4.000000}}
        Map<Integer, DoubleSummaryStatistics> collectMap4 = persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.summarizingDouble(Person::getId)));
        System.out.println(collectMap4);

        // 8. 取得分组的统计 记录条数  {1=1, 2=1, 3=1, 4=2}
        Map<Integer, Long> collectMap5 = persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.counting()));
        System.out.println(collectMap5);
    }

    /**
     *
     * FlatMap主要是用于stream合并：
     *
     * 通过以上示例代码，很容易发现其实map主要是用于遍历每个参数，然后进行参数合并或者返回新类型的集合。
     * FlatMap主要是用于stream合并，这个功能非常实用，他是默认实现多CPU并行执行的，所以我们合并集合优先实用这种方式。
     */
    @Test
    public void testFlatMap() {
        Person p1 = new Person(1, "p1", 11, new Date(), 0);
        Person p2 = new Person(2, "p2", 12, new Date(), 0);
        Person p3 = new Person(3, "p3", 13, new Date(), 0);
        Person p4 = new Person(4, "p4", 14, new Date(), 0);
        Person p5 = new Person(5, "p5", 15, new Date(), 0);
        Person p6 = new Person(6, "p6", 16, new Date(), 0);
        Person p7 = new Person(7, "p7", 17, new Date(), 0);
        Person p8 = new Person(8, "p8", 18, new Date(), 0);
        Person p9 = new Person(9, "p9", 19, new Date(), 0);
        List<Person> list1 = new ArrayList<>();
        list1.add(p1);
        list1.add(p2);
        list1.add(p3);
        list1.add(p4);
        List<Person> list2 = new ArrayList<>();
        list2.add(p5);
        list2.add(p6);
        list2.add(p7);
        list2.add(p8);
        list2.add(p9);

        // 参数 Function<? super T, ? extends Stream<? extends R>> mapper 需要继承Stream
        // 合并流
        System.out.println("合并流后转为list***");
        List<Person> collect = Stream.of(list1, list2).flatMap(eleList -> eleList.stream()).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("合并流后打印所有元素*** 每个元素为Person对象");
        Stream.of(list1, list2).flatMap(eleList -> eleList.stream()).forEach(System.out::println);


        // 打印的是集合
        System.out.println("forEach*** 按每个元素是list打印");
        Stream.of(list1, list2).forEach(System.out::println);
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
    public void testLimitAndSkip() {
        // list转set
        System.out.println("处理前*****");
        dataSet.parallelStream().forEach(item -> {
            System.out.println(item);
        });
        System.out.println("处理后*****");
        dataSet.parallelStream().limit(3).forEach(item -> {
            System.out.println(item);
        });

        System.out.println("skip跳过指定条数");
        persons.stream().skip(2).forEach(System.out::println);
        System.out.println("limit截取指定条数指定条数");
        persons.stream().limit(2).forEach(System.out::println);
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
    public void testMatch() {
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

    /**
     * 减少操作
     */
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

    @Test
    public void testFindAnyAndFindFirst() {
        // 1. 找到任务一个
        // stream()保证顺序一般就是列表当前第一个元素
        Optional<Person> any = persons.stream().findAny();
        // 输出
        any.ifPresent(System.out::println);
        System.out.println(any.get());
        // parallelStream()这个不保证顺序可能是任何一个
        Optional<Person> anyParallel = persons.parallelStream().findAny();
        // 输出
        anyParallel.ifPresent(System.out::println);
        System.out.println(anyParallel.get());

        // 第一个元素
        Optional<Person> first = persons.stream().findFirst();
        first.ifPresent(System.out::println);

        // 第一个元素 和 stream()结果相同
        Optional<Person> firstParallel = persons.parallelStream().findFirst();
        firstParallel.ifPresent(System.out::println);
    }


    @Test
    public void testSorted() {
        // 1. 数字排序
        List<Integer> integers = Arrays.asList(5, 3, 1, 4, 2, 6);
        integers.stream().sorted().forEach(System.out::println);

        // 排序正常, 打印乱序
/*        System.out.println("******* 并行流不保证foreach顺序");
        integers.stream().parallel().sorted().forEach(System.out::println);
        System.out.println("******* 并行流不保证foreach顺序");
        integers.parallelStream().sorted().forEach(System.out::println);*/
        // 正常排序
        List<Integer> collectIntegers = integers.parallelStream().sorted().collect(Collectors.toList());
        System.out.println(collectIntegers);

        // 2. 对象排序
        // 按年龄从小到大排序并打印
        Stream<Person> sorted = persons.stream().sorted(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
//                return 0;
                return o1.getAge() - o2.getAge();
            }
        });
        sorted.forEach(System.out::println);

        // 按年龄从小到大排序并打印
        persons.stream().sorted((o1, o2) -> {
            return o1.getAge() - o2.getAge();
        }).forEach(System.out::println);

        // 默认排序, 对象排序需要Comparator
        // 下面这句报错: java.lang.ClassCastException: per.lwp.java.lambda.Person cannot be cast to java.lang.Comparable
//        persons.stream().sorted().forEach(System.out::println);
    }

    /**
     * distinct去重数据
     * 使用Object#equals(Object)方法去重数据 distinct, 对象去重需要重写equals方法
     */
    @Test
    public void testDistinct() {

        // 1.integer去重
        List<Integer> integers = Arrays.asList(1, 3, 1, 3, 2, 6);
        integers.stream().distinct().forEach(System.out::println);
        System.out.println("并行流去重数据- 正常去重数据");
        integers.parallelStream().distinct().forEach(System.out::println);
        // 按集合顺序打印
        integers.parallelStream().distinct().forEachOrdered(System.out::println);

        // 2. 字符串去重
        List<String> strList = Arrays.asList("a", "b", "a", "bc", "bc");
        strList.stream().distinct().forEach(System.out::println);


        // 3. 对象去重需要重写equals方法
        List<Person> personList = Arrays.asList(
                new Person(1, "张三1", 33, new Date(), 0),
                new Person(1, "张三1", 33, new Date(), 0),
                new Person(2, "张三22", 33, new Date(), 0)
        );
        // 未去重,需要重写equals方法
        personList.stream().distinct().forEach(System.out::println);

    }
    @Test
    public void testMaxAndMinAndPeek() {
        List<Integer> integers = Arrays.asList(1, 3, 1, 3, 2, 6);
        // 最大年龄
        System.out.println("年龄最大的记录");
        Optional<Person> max = persons.stream().max((obj1, obj2) -> obj1.getAge() - obj2.getAge());
        System.out.println(max.get());
        // 最小年龄
        System.out.println("年龄最小的记录");
        Optional<Person> min = persons.stream().min((obj1, obj2) -> obj1.getAge() - obj2.getAge());
        min.ifPresent(System.out::println);

        System.out.println(persons.size());
        /* 不执行打印
        persons.stream().peek(item -> {
            System.out.println(item);
        });*/
        System.out.println("**********");
        persons.stream().peek(item -> {
            // 偷看数据
            System.out.println(item);
            // 可以修改对应值
            item.setAge(100);
        }).collect(Collectors.toList());
        System.out.println(persons);
    }
}
