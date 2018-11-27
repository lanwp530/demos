package per.lwp.bio.timeserver;

import per.lwp.ArrayUtils;
import per.lwp.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * description: 优化时间服务器,使用线程池
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class TimeServerOptimize {

//    private ExecutorService sxecutorService = Executors.newFixedThreadPool(100);
    private static int coreThreadsNum = 50;
    private static int maxThreadsNum = 200;
    private static ExecutorService executorService = new ThreadPoolExecutor(coreThreadsNum, maxThreadsNum,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());;

    public static void main(String[] args) {
        int port = 8080;
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            System.out.println(String.format("The time server is start. port: %s", port));
            Socket socket = null;
            while (true) {
                socket = server.accept();
                executorService.execute(new TimeServerHandler(socket));
//                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(server);
        }
    }
}
