package per.lwp.nio;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/28 14:25
 */
public class TimeNioClientTest {
    public static void main(String[] args) {
        TimeNioClient client = new TimeNioClient(null,8080);
        new Thread(client).start();
    }
}
