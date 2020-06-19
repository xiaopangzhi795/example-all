package com.geek45.exampleall.io.nio.demo5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 这个是代理服务端
 */
public class ProxyServer {

    public static void main(String[] args) throws IOException {
        proxy2();
    }

    private static void proxy2() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10081));
        //拿到客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.err.println("接收到连接");
        //客户端的消息
        ByteBuffer read = ByteBuffer.allocate(1024);
        //要返回给客户端的消息
        ByteBuffer clientMsg = ByteBuffer.allocate(1024);
        int length2;
        retry:
        while (true) {
            read.clear();
            int length = socketChannel.read(read);
            if (length > 0) {
                read.flip();
                //读取到了客户端的消息，在这将客户端的消息转发给真实服务端
                SocketChannel server = SocketChannel.open();
                server.connect(new InetSocketAddress("127.0.0.1", 10082));
                System.err.println("连接上真实服务器");
                retry2:
                while (true) {
                    if (server.finishConnect()) {
                        server.write(ByteBuffer.wrap(read.array(), 0, length));
                        System.err.println("消息转发给服务器");
                        length2 = server.read(clientMsg);
                        if (length > 0) {
                            break retry;
                        }
                    }
                }
                //System.err.println(new String(read.array(), 0, length));
            }else{
                System.out.println("没有消息");
            }
        }
        clientMsg.flip();
        ByteBuffer byteBuffer = ByteBuffer.wrap(clientMsg.array(), 0, length2);
        socketChannel.write(byteBuffer);
        System.err.println("消息回复完毕");
        socketChannel.close();
        serverSocketChannel.close();
    }

    /**
     * 第一版，先简单和客户端连接起来，实现消息的接收和发送
     * @throws IOException
     */
    private static void proxy() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10081));
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
