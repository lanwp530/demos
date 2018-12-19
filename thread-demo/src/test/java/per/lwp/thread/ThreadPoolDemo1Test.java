package per.lwp.thread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by lawnp on 2018/12/6 14:44
 */
public class ThreadPoolDemo1Test {

    ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 启动两个线程
     */
    @Test
    public void oneTest() {
        executorService.execute(new ThreadPoolDemo1Runner());
        executorService.execute(new ThreadPoolDemo1Runner());

        printPoolInfo(executorService);

        executorService.shutdown();  // 启动一个有序的关机，在以前提交的任务被执行，但没有新的任务将被接受
//        executorService.shutdownNow(); // 试图阻止所有积极执行任务，停止等待任务的处理，并返回一个列表，正在等待执行的任务
        printPoolInfo(executorService);

        while (!executorService.isTerminated()) {
            System.out.println(executorService.isTerminated());
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printPoolInfo(executorService);
    }

    private void printPoolInfo(ExecutorService executorService) {
        System.out.println(String.format("pool isShutdown: %s, isTerminated: %s", executorService.isShutdown(), executorService.isTerminated()));
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

class ThreadPoolDemo1Runner implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is running");
    }
}

class ThreadPoolDemo1Runner1 implements Runnable {
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

class ThreadPoolDemo1Runner1_2 implements Runnable {

    private CountDownLatch latch;

    public ThreadPoolDemo1Runner1_2(CountDownLatch latch) {
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
