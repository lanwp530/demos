package per.lwp.bio.timeserver;

import per.lwp.ArrayUtils;
import per.lwp.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class TimeClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        socketClient(host, port);
    }

    /**
     * 读写带换行符
     * @param host
     * @param port
     */
    private static void socketClient(String host, int port) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(host, port);
            System.out.println(String.format("The time client is start. host: %s , port: %s", host, port));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // autoFlush=true,自动刷新功能针对的是println方法,print方法无效
            out = new PrintWriter(socket.getOutputStream(), true);

            String order = "query time order";
            out.println(order);
            /*out.print(order);
            out.flush();*/
            System.out.println("send server success!");
            String nowTimeStr = in.readLine();
            System.out.println(String.format("now : %s", nowTimeStr));

        } catch (IOException e) {
            e.printStackTrace();
            // java.net.SocketException: Connection reset 服务器断开异常
        } finally {
            IOUtils.close(out);
            IOUtils.close(in);
            IOUtils.close(socket);
        }
    }
}
