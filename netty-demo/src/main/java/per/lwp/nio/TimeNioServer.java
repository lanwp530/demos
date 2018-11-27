package per.lwp.nio;

import per.lwp.ArrayUtils;
import per.lwp.IOUtils;
import per.lwp.bio.timeserver.TimeServerHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class TimeNioServer implements Runnable{

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private volatile boolean stop;
    // 自定义默认backlog
    private static final int BACKLOG = 1024;

    public TimeNioServer(int port) {
        try {
            // 1. 打开服务器管道ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();

            // 2. 监听端口 InetAddress.getByName("IP"); 默认BACKLOG=50
            serverSocketChannel.socket().bind(new InetSocketAddress(port), BACKLOG);
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);

            // 3. 创建selector-选择器   Selector.open() 使用 SelectorProvider.provider().openSelector()
            this.selector = Selector.open();

            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // IOUtils.close(serverSocketChannel); 不用关闭通道,否则外部访问不可用
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
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
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
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
            // 处理新接进来的请求
            if (key.isAcceptable()) {
                //  add  new connection
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                // 注册OP_READ读事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                // read data
                SocketChannel socketChannel = (SocketChannel) key.channel();
                // 获取(分配)内存
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();  // 翻转设置位置为0
                    // 创建和数据长度一样长度的数组,缓存区readBuffer数据放入bytes中
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String reqBody = new String(bytes, "UTF-8");

                    System.out.println(String.format("The time server recive order : %s", reqBody));
                    String currentTime = "query time order".equalsIgnoreCase(reqBody) ? new Date().toString() : "BAD ORDER";
                    doWrite(socketChannel, currentTime);
                }
            }

            /*if (key.isWritable()) {

            }
            if (key.isConnectable()) {

            }*/

        }
    }

    // nio都是基于Buffer
    public void doWrite(SocketChannel socketChannel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] writeBytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(writeBytes.length);
            writeBuffer.put(writeBytes);
            writeBuffer.flip(); // 翻转
            socketChannel.write(writeBuffer);
        }
    }
}
