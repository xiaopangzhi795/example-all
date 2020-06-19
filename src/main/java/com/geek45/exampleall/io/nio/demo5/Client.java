package com.geek45.exampleall.io.nio.demo5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 这个是客户端
 */
public class Client {

    public static void main(String[] args) throws IOException {
        client2();
    }

    /**
     * 第二版，
     * 请求连接附带真实的ip和端口
     * @throws IOException
     */
    private static void client2() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 10081));
        System.err.println("链接上服务器");
        retry:
        while (true) {
            String header = "GET / HTTP/1.1\n" +
                    "Host: 127.0.0.1:10080\n" +
                    "Connection: Upgrade\n" +
                    "Pragma: no-cache\n" +
                    "Cache-Control: no-cache\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36\n" +
                    "Upgrade: websocket\n" +
                    "Origin: http://www.websocket-test.com\n" +
                    "Sec-WebSocket-Version: 13\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: zh-CN,zh;q=0.9\n" +
                    "Cookie: SESSION=qn6lcqeg9sgvio0gjpakaga3fq\n" +
                    "Sec-WebSocket-Key: RcRJezYfkOQvIPX1DSiPnA==\n" +
                    "Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits";
            if (socketChannel.finishConnect()) {
                ByteBuffer head = ByteBuffer.wrap(header.getBytes());
                ByteBuffer byteBuffer = ByteBuffer.wrap("你好".getBytes());
                socketChannel.write(new ByteBuffer[]{head, byteBuffer});
                System.err.println("发送完毕，开始接收返回消息");
                ByteBuffer read = ByteBuffer.allocate(1024);
                int length = socketChannel.read(read);
                if (length > 0) {
                    System.err.println(new String(read.array(), 0, length));
                }
                System.err.println("接收到消息");
                break retry;

            }
        }
        socketChannel.close();
    }

    /**
     * 第一版，先简单链接一下
     * @throws IOException
     */
    private static void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 10081));
        System.err.println("链接上服务器");
        retry:
        while (true) {
            if (socketChannel.finishConnect()) {
                ByteBuffer byteBuffer = ByteBuffer.wrap("你好".getBytes());
                socketChannel.write(byteBuffer);
                System.err.println("发送完毕，开始接收返回消息");
                ByteBuffer read = ByteBuffer.allocate(1024);
                int length = socketChannel.read(read);
                if (length > 0) {
                    System.err.println(new String(read.array(), 0, length));
                }
                System.err.println("接收到消息");
                break retry;

            }
        }
        socketChannel.close();
    }
}
