package per.lwp.thread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * created by lawnp on 2018/12/6 14:44
 */
public class ThreadCallableDemo1Test {

    ExecutorService executorServicePool = Executors.newCachedThreadPool();

    /**
     * 启动两个线程
     */
    @Test
    public void oneTest() throws ExecutionException, InterruptedException {
        Future<CallableObject> future = executorServicePool.submit(new ThreadDemo1Callable());
        CallableObject result = future.get();  // 一直阻塞到任务完成
        System.out.println("result：" + result.isSuccess() + " " + result.getAttachment());
        System.out.println("主线程结束...");

    }
    
    /**
     * 主线程启动两个线程,两个线程都需要处理完成后,继续主线程任务
     */
    @Test
    public void allThreadOverOneTest() throws ExecutionException, InterruptedException {
        Future<CallableObject> future1 = executorServicePool.submit(new ThreadDemo1Callable());
        Future<CallableObject> future2 = executorServicePool.submit(new ThreadDemo1Callable());
        CallableObject result1 = future1.get();  // 一直阻塞到任务完成
        CallableObject result2 = future2.get();  // 一直阻塞到任务完成
        print(result1);
        print(result2);
        System.out.println("主线程结束...");
    }

    /**
     * 主线程启动两个线程,两个线程都需要处理完成后,继续主线程任务
     */
    @Test
    public void allThreadOverTwoTest() throws ExecutionException, InterruptedException {
        FutureTask<CallableObject> task = new FutureTask<>(new ThreadDemo1Callable());
        Thread t1 = new Thread(task);
        t1.start();
        CallableObject result = task.get();  // 一直阻塞到任务完成
        print(result);
        System.out.println("主线程结束...");
    }

    private void print(CallableObject result) {
        System.out.println("result：" + result.isSuccess() + " " + result.getAttachment());
    }
}

class ThreadDemo1Callable implements Callable<CallableObject> {
    @Override
    public CallableObject call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " is running");

        Thread.sleep(2000);

        CallableObject result = new CallableObject();
        result.setSuccess(true);
        result.setAttachment("test");
        return result;
    }
}

class CallableObject {
    boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    Object attachment;
}

