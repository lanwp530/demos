package per.lwp.bio;

import per.lwp.IOUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * description:
 * 服务端单线程处理socket
 * @author lanwp
 * @date 2018/11/26 12:47
 */
public class SockerServerSingle {

    private static final int PRORT = 20000;

    public static void main(String[] args) throws IOException {
        SockerServerSingle demo = new SockerServerSingle();
        ServerSocket server = demo.createServerSocketDefault(PRORT);
        Socket socket = null;
        while ((socket = server.accept()) != null) {
            demo.handle(socket);
        }
        IOUtils.close(server);
    }

    public ServerSocket createServerSocketDefault(int port) throws IOException {
//        ServerSocket server = new ServerSocket(10000); // backlog 默认50
        ServerSocket server = new ServerSocket(port, 50);
        return server;
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        ServerSocket server = new ServerSocket();
        // 绑定端口
        server.bind(new InetSocketAddress(port));
        return server;
    }

    public void handle(Socket socket) {
        if (socket == null) {
            return;
        }

        InputStream is = null;
        BufferedReader br = null;

        PrintWriter pw = null;
        try {
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            // 没有断开，输入流一直存在,读行需要内容带换行符\n
            String result = br.readLine();
            System.out.println(result);

            // 获取客户端输出流
            pw = new PrintWriter(socket.getOutputStream());
            pw.write("服务器收到了信息: " + result);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(pw);
            IOUtils.close(br);
        }
    }
}
