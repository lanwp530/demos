package per.lwp.lock;

import java.util.concurrent.locks.Lock;

/**
 * created by lawnp on 2018/12/19.
 */
public class SynchronizedTest {
    public static void main(String[] args) {
        SynchronizedTest st = new SynchronizedTest();
        // 只有一个线程能够使用
        new Thread(() -> {
            st.test1();
        }).start();
        new Thread(() -> {
            st.test2();
        }).start();

        new Thread(() -> {
            st.test3();
        }).start();
    }

    public synchronized void test1() {
        System.out.println("test1 ... start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test1 ... end");
    }

    public synchronized void test2() {
        System.out.println("test2 ... start");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test2 ... end");
    }

    /**
     * 使用同步块
     */
    public void test3() {
        System.out.println("test3 ... in");
        synchronized (this) {
            System.out.println("test3 ... start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test3 ... end");
        }
    }

}
