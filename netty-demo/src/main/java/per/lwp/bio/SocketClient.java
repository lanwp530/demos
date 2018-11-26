package per.lwp.bio;

import per.lwp.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * description:
 *   idea alt + enter 抛出异常
 *
 * @author lanwp
 * @date 2018/11/26 12:46
 */
public class SocketClient {
    private static final int PORT = 20000;
    private static final String SERVER_IP = "127.0.0.1";
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, PORT);

        PrintWriter pw = null;
        BufferedReader br = null;
        try {
            pw = new PrintWriter(socket.getOutputStream());
            pw.write("hello server!\n");
            // 发送信息
            pw.flush();
            //关闭输出流
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(pw);
            IOUtils.close(br);
        }

        // 关闭
        socket.close();
    }
}
