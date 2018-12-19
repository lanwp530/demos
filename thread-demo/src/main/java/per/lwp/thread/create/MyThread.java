package per.lwp.thread.create;

/**
 * created by lawnp on 2018/12/19.
 */
public class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println(this.getClass().getName() + " " + Thread.currentThread().getName() +" is running");
    }
}
