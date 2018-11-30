package per.lwp.nio;

import java.util.HashMap;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class TimeNioServerTest{

    public static void main(String[] args) {
        TimeNioServer server = new TimeNioServer(8080);

        new Thread(server).start();

        HashMap
    }
}
