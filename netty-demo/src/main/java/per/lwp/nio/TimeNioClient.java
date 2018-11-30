package per.lwp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class TimeNioClient implements Runnable{

    private SocketChannel clientChannel;

    private Selector selector;

    private volatile boolean stop;
    // 自定义默认backlog
    private static final int BACKLOG = 1024;

    private String host;
    private int port;

    private static final String LOCAL_HOST = "127.0.0.1";

    public TimeNioClient(String host, int port) {
        this.host = host == null ? LOCAL_HOST : host;
        this.port = port;
        try {
            // 1. 打开服务器管道ServerSocketChannel
            clientChannel = SocketChannel.open();

            // 2. 配置
            // 设置为非阻塞
            clientChannel.configureBlocking(false);
            Socket socket = clientChannel.socket();
            // 重用
            socket.setReuseAddress(true);
//            System.out.println(socket.getReceiveBufferSize());  // 默认65536 64k
//            System.out.println(socket.getSendBufferSize());  // 默认65536  64k
//            socket.setReceiveBufferSize(8192);
//            socket.setSendBufferSize(8192);

            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void doConnect() throws IOException {
        boolean connected = clientChannel.connect(new InetSocketAddress(this.host, this.port));
        if (connected) {
            clientChannel.register(selector, SelectionKey.OP_READ);
            doWrite(clientChannel, "QUERY TIME ORDER");
        } else {
            clientChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {

        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                // 实际使用this.selector.select() 可以参考netty实现
                this.selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handle(key);
                    } catch (Exception e) {
                        if (key != null) {
                            // 取消选择器, 关闭通道
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                    // 远程断开时打印异常
//                        e.printStackTrace(); // java.io.IOException: 远程主机强迫关闭了一个现有的连接
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        // 多路复用器关闭后,所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handle(SelectionKey key) throws IOException{
        if (key.isValid()) {
            // 判断是否连接成功
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    // 注册OP_READ读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite(clientChannel, "QUERY TIME ORDER");
                } else {
                    System.exit(1);
                }
            }

            if (key.isReadable()) {
                // read data
                // 获取(分配)内存
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();  // 翻转设置位置为0
                    // 创建和数据长度一样长度的数组,缓存区readBuffer数据放入bytes中
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String reqBody = new String(bytes, "UTF-8");

                    System.out.println(String.format("Recived from the server time: %s", reqBody));
                    this.stop = true;
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    key.cancel();
                    socketChannel.close();
                }
            }

            /*if (key.isWritable()) {

            }
            if (key.isConnectable()) {

            }*/

        }
    }

    // nio都是基于Buffer
    public void doWrite(SocketChannel socketChannel, String req) throws IOException {
        if (req != null && req.trim().length() > 0) {
            byte[] writeBytes = req.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(writeBytes.length);
            writeBuffer.put(writeBytes);
            writeBuffer.flip(); // 翻转
            socketChannel.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {
                System.out.println("send order 2 server success !");
            }
        }
    }
}
