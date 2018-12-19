package per.lwp.java.optional;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * created by lawnp on 2018/12/19.
 */
public class OptionalTest {

    /**
     * 实例 Optional
     */
    @Test(expected = NoSuchElementException.class)
    public void testOptionalEmptyException() {
        // 创建一个空Optional对象
        Optional optionalEmpty = Optional.empty();
        // isPresent() 判断value是否为null， null返回 false ,非null返回true
        System.out.println(optionalEmpty.isPresent());
        // 空值get抛出 异常
        System.out.println(optionalEmpty.get() == null);
    }

    /**
     * 常用
     */
    @Test
    public void testOptionalOf() {
        Integer integer = 99;
        // 1. Optional.of() 不允许传入null, 传入null参数,抛出异常
        Optional optional = Optional.of(integer);
//        Optional<Integer> optional = Optional.of(integer);
        System.out.println(String.format("optional中是否有值: %s", optional.isPresent()));
        System.out.println(String.format("optional值: %s", optional.get()));
        System.out.println("optioanl lambda表达式,有值打印, null不打印");
        optional.ifPresent(item -> System.out.println(item));
        optional.ifPresent(System.out::println);

        // 2. Optional.ofNullable(） 允许传入null
        Optional nullOptional = Optional.ofNullable(null);
        System.out.println(nullOptional.isPresent());
    }

    @Test
    public void testOptionalFilter() {
        // 创建一个空Optional对象
        Integer integer = 99;
        Optional<Integer> optional = Optional.of(integer);
        // 过滤小于2的值，只保留大于2的
        optional.filter((Integer item) -> {
            return item > 2;
        }).ifPresent(System.out::println);
        // 过滤小于100，保留大于100的值
        System.out.println("******过滤小于100，保留大于100的值******");
        optional.filter((Integer item) -> item > 100).ifPresent(System.out::println);
    }

    @Test
    public void testOptionalGet() {
        // 创建一个空Optional对象
        Integer integer = 99;
        Optional<Integer> optional = Optional.of(integer);
        // 有值取值,值为null则抛出异常throw new NoSuchElementException("No value present")
        System.out.println(optional.get());

        // 推荐-非null执行,为null不执行的代码
        optional.ifPresent(value -> {
            // do something
            System.out.println(value);
        });
    }

    @Test
    public void testOptionalOrElse() {
        Integer integer = 99;
        Optional<Integer> optional = Optional.of(integer);
        optional.ifPresent(item -> {
            // 非null执行

        });

        // optional.get() 直接取value, value为null抛出异常
        // optional.orElse() value为空则取另一个值
        System.out.println("optionalOrElse *****");
        Optional<Integer> optionalOrElse = Optional.ofNullable(integer);
        System.out.println(optionalOrElse.orElse(123));
        // 传入null
        optionalOrElse = Optional.ofNullable(null);
        // optionalOrElse为null时返回
        System.out.println(optionalOrElse.orElse(123));

        // value=null, 返回复杂运算后的值
        Integer orElseGetInteger = optionalOrElse.orElseGet(() -> {
            // 为null则直接给值, 需要函数处理的值
            return 888;
        });
        System.out.println(orElseGetInteger);

        // value=null时，抛出自定义异常
        Integer exceptionInteger = optionalOrElse.orElseThrow(() -> {
            return new RuntimeException("测试异常");
        });
        System.out.println(exceptionInteger);
    }

    @Test
    public void testOptionalMap() {
        Integer integer = 99;
        Optional<Integer> optional = Optional.of(integer);
        System.out.println(optional.map(value -> value + 999).get());
        // 对象类似处理map
    }

    @Test
    public void testOptionalFlatMap() {
        Integer integer = 99;
        Optional<Integer> optional = Optional.of(integer);
        // Function<? super T, Optional<U>> mapper  传入value类型值, 返回 Optional<U>类型(Optional中U返回类型)
        Integer integer1 = optional.flatMap(value -> Optional.of(value + 999)).get();
        System.out.println(integer1);
    }

}
