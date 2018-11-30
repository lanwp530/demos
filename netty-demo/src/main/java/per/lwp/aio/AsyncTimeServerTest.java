package per.lwp.aio;

import per.lwp.ArrayUtils;
import per.lwp.aio.server.AsyncTimeServerHandler;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/29 9:05
 */
public class AsyncTimeServerTest {
    public static void main(String[] args) {
        int port = 8080;
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        new Thread(new AsyncTimeServerHandler(port)).start();
    }
}
