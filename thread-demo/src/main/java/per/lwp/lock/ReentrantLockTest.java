package per.lwp.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * created by lawnp on 2018/12/19.
 */
public class ReentrantLockTest implements Runnable{

    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
            try {
                lock.lock();  // 看这里就可以
                for (int j = 0; j < 10000; j++) {
                    //lock.lock(); ①
                    i++;
                }
                System.out.println(i);
            } finally {
                lock.unlock(); // 看这里就可以
                //lock.unlock();②
            }
    }


    public static void main(String[] args) {
        new Thread(new ReentrantLockTest()).start();
        new Thread(new ReentrantLockTest()).start();
    }
}
