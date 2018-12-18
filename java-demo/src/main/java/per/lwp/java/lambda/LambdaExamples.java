package per.lwp.java.lambda;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * //        Collectors.summingInt(); // 和        Collectors.toSet()
 * //        Collectors.toList()
 * //        Collectors.toMap()
 * //        Collectors.toCollection();
 * //        Collectors.toConcurrentMap()
 * //        Collectors.groupingBy()
 * //        Collectors.groupingByConcurrent()
 * //        Collectors.joining();
 * //        Collectors.averagingDouble() //平均数
 * //        Collectors.summarizingDouble()
 * //        Collectors.counting() // 统计条数
 * //        Collectors.maxBy() // 最大
 * //        Collectors.minBy()  // 最小
 * //        Collectors.joining()  // join
 * //        Collectors.reducing()
 * //        Collectors.collectingAndThen()
 * //        Collectors.mapping()
 * //        Collectors.partitioningBy()
 *
 * created by lawnp on 2018/12/17.
 */
public class LambdaExamples {

    List<String> languages = Arrays.asList("Java", "html5","JavaScript", "C++", "hibernate", "PHP");
    private static List<String> list = Arrays.asList("abc", "3", "1", "2", "abb", "名字1", "A", "ccc");
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
        persons.add(new Person(4, "马六41", 22, new Date(), 1));
        persons.add(new Person(4, "马六42", 22, new Date(), 1));
    }

    /**
     * Stream<T> filter(Predicate<? super T> predicate)
     */
    @Test
    public void testFilter() {
        // 过滤, return true的数据保留下来
        list.parallelStream().filter((String item) -> item.equals("ccc")).forEach(System.out::println);
        // 等价于
        list.parallelStream().filter((String item) -> {
            return item.equals("ccc");
        }).forEach(item -> {
            System.out.println(item);
        });
    }

//    @Test
//    public void testFilter() {
        /*//开头是J的语言
        filter(languages,(name)->name.startsWith("J"));
        //5结尾的
        filter(languages,(name)->name.endsWith("5"));
        //所有的语言
        filter(languages,(name)->true);
        //一个都不显示
        filter(languages,(name)->false);
        //显示名字长度大于4
        filter(languages,(name)->name.length()>4);
        System.out.println("-----------------------");
        //名字以J开头并且长度大于4的
        Predicate<String> c1 = (name)->name.startsWith("J");
        Predicate<String> c2 = (name)->name.length()>4;
        filter(languages,c1.and(c2));

        //名字不是以J开头
        Predicate<String> c3 = (name)->name.startsWith("J");
        filter(languages,c3.negate());

        //名字以J开头或者长度小于4的
        Predicate<String> c4 = (name)->name.startsWith("J");
        Predicate<String> c5 = (name)->name.length()<4;
        filter(languages,c4.or(c5));

        //名字为Java的
        filter(languages,Predicate.isEqual("Java"));

        //判断俩个字符串是否相等
        boolean test = Predicate.isEqual("hello").test("world");*/
//    }

    /**
     * 常用 list转 map 分组功能
     * list转 map
     */
    @Test
    public void testMap() {

        // 转list 将persons中的 name转为list  map可以做属性操作或者原值
        List<String> aa = persons.stream().map(a -> a.getName()).collect(Collectors.toList());
        aa.forEach(a->System.out.println(a));

        List<Person> personList = persons.stream().map(a -> a).collect(Collectors.toList());
        // 上面等同于List<Person> personList = persons.stream().collect(Collectors.toList());
        personList.forEach(a->System.out.println(a));

        // 将集合转为MMap<String, String> key=name val=name  Collectors.toMap(key, val)
        Map<String, String> collectMap1 = persons.stream().collect(Collectors.toMap(Person::getName, Person::getName));
        System.out.println(collectMap1);

        // 分组根据名称分组  Map<String, List<Person>>
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

        // 取得所有数据总和 14
        Integer allSum = persons.stream().collect(Collectors.summingInt(Person::getId));
        System.out.println(allSum);
        // 取得分组总和  {1=1, 2=2, 3=3, 4=8}
        Map<Integer, Integer> collectMap3 = persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.summingInt(Person::getId)));
        System.out.println(collectMap3);

        // 汇总统计信息{1=DoubleSummaryStatistics{count=1, sum=1.000000, min=1.000000, average=1.000000, max=1.000000}, 2=DoubleSummaryStatistics{count=1, sum=2.000000, min=2.000000, average=2.000000, max=2.000000}, 3=DoubleSummaryStatistics{count=1, sum=3.000000, min=3.000000, average=3.000000, max=3.000000}, 4=DoubleSummaryStatistics{count=2, sum=8.000000, min=4.000000, average=4.000000, max=4.000000}}
        Map<Integer, DoubleSummaryStatistics> collectMap4 = persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.summarizingDouble(Person::getId)));
        System.out.println(collectMap4);

        // 取得分组的统计 记录条数  {1=1, 2=1, 3=1, 4=2}
        Map<Integer, Long> collectMap5 = persons.stream().collect(Collectors.groupingBy(Person::getId, Collectors.counting()));
        System.out.println(collectMap5);
    }
}
