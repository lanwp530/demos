package per.lwp.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * created by lawnp on 2018/12/19.
 */
public class ReentrantLock1Test implements Runnable{

    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
            try {
                lock.lock();  // 看这里就可以
                System.out.println("线程进来了");
                i++;
                System.out.println(i);
                System.out.println("线程进结束了");
            } finally {
                lock.unlock(); // 看这里就可以
                //lock.unlock();②
            }
    }


    public static void main(String[] args) {
        new Thread(new ReentrantLock1Test()).start();
        new Thread(new ReentrantLock1Test()).start();
    }
}
