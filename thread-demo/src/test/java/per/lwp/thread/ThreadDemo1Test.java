package per.lwp.thread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by lawnp on 2018/12/6 14:44
 */
public class ThreadDemo1Test {

    /**
     * 启动两个线程
     */
    @Test
    public void oneTest() {
        Thread t1 = new Thread(new ThreadDemo1Runner(), "线程1");
        Thread t2 = new Thread(new ThreadDemo1Runner(), "线程2");
        t1.start();
        t2.start();
    }

    /**
     * 主线程启动两个线程,两个线程都需要处理完成后,继续主线程任务
     */
    @Test
    public void allThreadOverOneTest() {
        Thread t1 = new Thread(new ThreadDemo1Runner1(), "线程1");
        Thread t2 = new Thread(new ThreadDemo1Runner1(), "线程2");
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程结束...");
    }

    /**
     * 主线程启动两个线程,两个线程都需要处理完成后继续主线程任务
     */
    @Test
    public void allThreadOverTwoTest() {
        CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread(new ThreadDemo1Runner1_2(latch), "线程1");
        Thread t2 = new Thread(new ThreadDemo1Runner1_2(latch), "线程2");
        t1.start();
        t2.start();
        try {
            latch.await();  // 阻塞等待线程执行完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程结束... method: allThreadOverTwoTest");
    }

}

class ThreadDemo1Runner implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is running");
    }
}

class ThreadDemo1Runner1 implements Runnable {
    @Override
    public void run() {
        int seconds = new Random().nextInt(8) + 2;
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + String.format(" is running , sleep: %ss", seconds));
    }
}

class ThreadDemo1Runner1_2 implements Runnable {

    private CountDownLatch latch;

    public ThreadDemo1Runner1_2(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        int seconds = new Random().nextInt(8) + 2;
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + String.format(" is running , sleep: %ss", seconds));

        latch.countDown();
    }
}
