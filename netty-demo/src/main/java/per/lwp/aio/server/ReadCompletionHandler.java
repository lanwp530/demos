package per.lwp.aio.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/29 9:29
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    AsynchronousSocketChannel clientChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel) {
        this.clientChannel = asynchronousSocketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String reqBody = new String(body, "UTF-8");
            System.out.println(String.format("The time server receive order : %s", reqBody));
            String currentTime = "query time order".equalsIgnoreCase(reqBody) ? new Date().toString() : "BAD ORDER";
            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        try {
            this.clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String currentTime) {
        if (currentTime != null && !currentTime.trim().isEmpty()) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            // 需要对写结果处理，如果无需处理则不需要CompletionHandler，clientChannel.write(writeBuffer)
            clientChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    // 如果没有发送完整,继续发送
                    if (buffer.hasRemaining()) {
                        clientChannel.write(buffer, buffer, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        clientChannel.close();
                    } catch (IOException e) {
                        // ingnore on close : e.printStackTrace();
                    }
                }
            });
        }
    }
}
