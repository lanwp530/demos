package per.lwp.thread;

/**
 * created by lawnp on 2018/12/19.
 */
public class WaitNotifyTest {

    public static Object lock = new Object();

    public static void main(String[] args) {
        test();

        // 在子线程wait()前执行唤醒,没有打印子线程后面的内容
        synchronized (lock) {
            System.out.println("执行了.. notify");
            lock.notify();
        }

        // 唤醒子线程, 等待子线程执行完
        while (Thread.activeCount() > 2){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                lock.notify();
            }
        }

        /*synchronized (lock) {
            lock.notifyAll();
        }*/
        System.out.println("主线程结束");
    }

    public static void test() {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("线程1： start");
                try {
                    lock.wait();
                    System.out.println(111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("线程2： start");
                try {
                    lock.wait();
                    System.out.println(222);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
