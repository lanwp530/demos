package per.lwp.java.interface1;

/**
 * created by lawnp on 2018/12/18.
 */
public interface Interface1 {


    default void test() {
        System.out.println("test");
    }

    default void test1() {
        System.out.println("test1");
    }
}
