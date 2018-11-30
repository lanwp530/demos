package per.lwp.aio;

import per.lwp.ArrayUtils;
import per.lwp.aio.client.AsyncTimeClientHandler;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/29 13:05
 */
public class AsyncTimeClientTest {
    public static void main(String[] args) {
        int port = 8080;
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String host = "127.0.0.1";
        new Thread(new AsyncTimeClientHandler(host, port)).start();
    }
}
