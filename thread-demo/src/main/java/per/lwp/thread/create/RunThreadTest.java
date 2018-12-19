package per.lwp.thread.create;

/**
 * created by lawnp on 2018/12/19.
 */
public class RunThreadTest {
    public static void main(String[] args) throws InterruptedException {

        new Thread(new MyRunnable()).start();
        new MyThread().start();

        // 这个线程不一定能打印出运行内容
        Thread thread = new Thread(new MyRunnable(), "守护线程A");
        // 默认daemon=false 非守护线程, 主线程关闭后，子线程任然在执行，除非停止虚拟机
        // daemon = true  守护线程, 运行的主线程停止后,无论子线程任务是否执行完都会停止子线程
        // 设置为守护线程
        thread.setDaemon(true);
        thread.start();

        System.out.println("返回在当前线程的 thread group及其子群活动线程的数量估计: " + Thread.activeCount());
        // idea中包括main线程 至少有2个线程在运行
        int i;
        long time = System.currentTimeMillis();
        // 超过3秒
//        while ((i = Thread.activeCount()) > 2 || (System.currentTimeMillis() - time > 3000)) {
        while (System.currentTimeMillis() - time < 3000) {
            System.out.println(Thread.activeCount());
            Thread.sleep(1000);
        }
        Thread[] tarray = new Thread[Thread.activeCount()];
        Thread.enumerate(tarray);
        for (int j = 0; j < tarray.length; j++) {
            // thread group及其子群活动线程 信息
            System.out.println(tarray[j]);
        }

        System.out.println("主线程执行完成...");
    }
}
