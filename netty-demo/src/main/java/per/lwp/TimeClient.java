package per.lwp;

import per.lwp.ArrayUtils;
import per.lwp.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(host, port);
            System.out.println(String.format("The time client is start. host: %s , port: %s", host, port));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String order = "query time order";
            out.println(order);
            System.out.println("send server success!");
            String nowTimeStr = in.readLine();
            System.out.println(String.format("now : %s", nowTimeStr));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(out);
            IOUtils.close(in);
            IOUtils.close(socket);
        }
    }
}
