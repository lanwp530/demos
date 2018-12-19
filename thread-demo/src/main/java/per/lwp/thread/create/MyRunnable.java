package per.lwp.thread.create;

/**
 * 两种方式创建线程
 * 1. 实现 Runnable接口
 * 2. 继承 Thread类
 * created by lawnp on 2018/12/19.
 */
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println(this.getClass().getName() + " " + Thread.currentThread().getName() +" is running");
    }
}
