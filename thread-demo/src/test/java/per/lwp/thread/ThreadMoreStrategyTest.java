package per.lwp.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * jdk1.8开始 CompletableFuture
 * created by lawnp on 2018/12/6 19:22
 */
public class ThreadMoreStrategyTest {
    ExecutorService executor = Executors.newCachedThreadPool();

    /**
     *  在JDK1.5已经提供了Future和Callable的实现,可以用于阻塞式获取结果,如果想要异步获取结果,通常都会以轮询的方式去获取结果,如下
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void oneTest() throws ExecutionException, InterruptedException {
        //定义一个异步任务
        Future<String> future = executor.submit(()->{
            Thread.sleep(2000);
            return "hello world";
        });
        //轮询获取结果
        while (true){
            if(future.isDone()) {
                System.out.println(future.get());
                break;
            }
        }
    }

    @Test
    public void t1Test() throws ExecutionException, InterruptedException {
        //定义一个异步任务
        Future<String> future = executor.submit(()->{
            Thread.sleep(2000);
            return "hello world";
        });
        //轮询获取结果
        /*while (!future.isDone()){
            System.out.println(future.isDone());
        }*/
        System.out.println(future.get());  // future.get()阻塞获取结果
    }

    @Test
    public void t2Test() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world";
        });
        System.out.println(11);
        System.out.println(future.get());  //阻塞的获取结果  ''helllo world"
        System.out.println(22);
    }
}
