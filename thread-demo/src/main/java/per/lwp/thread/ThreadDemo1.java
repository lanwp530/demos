package per.lwp.thread;

/**
 * created by lawnp on 2018/12/6 12:40
 */
public class ThreadDemo1 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
        }, "线程1");
        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
        }, "线程2");
        t1.start();
        t2.start();
    }
}
