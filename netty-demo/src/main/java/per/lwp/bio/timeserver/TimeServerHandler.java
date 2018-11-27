package per.lwp.bio.timeserver;

import per.lwp.ArrayUtils;
import per.lwp.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Date;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class TimeServerHandler implements Runnable{

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // 输出后自动flush发信息
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String reqBody = null;
            String currentTime = null;
            /*while (true) {
                reqBody = br.readLine();
                if (reqBody == null) {
                    break;
                }
            }*/
            // 读取到的内容为null跳出循环
            while ((reqBody = in.readLine()) != null) {
                System.out.println(String.format("The time server recive order : %s", reqBody));
                currentTime = "query time order".equalsIgnoreCase(reqBody) ? new Date().toString() : "BAD ORDER";
                out.println(currentTime);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(out);
            IOUtils.close(in);
            IOUtils.close(socket);
        }
    }
}
