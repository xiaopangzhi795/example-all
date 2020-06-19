package com.geek45.exampleall.io.nio.demo5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/***
 *  这个当做真实的服务端
 */
public class Server {

    public static void main(String[] args) throws IOException {
        server();
    }

    /**
     * 简单的服务端，实现简单消息接收和回复功能
     * @throws IOException
     */
    private static void server() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10082));
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.err.println("接收到连接");
        ByteBuffer read = ByteBuffer.allocate(1024);
        retry:
        while (true) {
            read.clear();
            System.err.println("111");
            int length = socketChannel.read(read);
            System.err.println("222");
            if (length > 0) {
                read.flip();
                System.err.println(new String(read.array(), 0, length));
                break retry;
            }else{
                System.out.println("没有消息");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap("收到".getBytes());
        socketChannel.write(byteBuffer);
        System.err.println("消息回复完毕");
        socketChannel.close();
        serverSocketChannel.close();
    }
}
