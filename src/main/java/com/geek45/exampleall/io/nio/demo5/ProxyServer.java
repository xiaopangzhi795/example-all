package com.geek45.exampleall.io.nio.demo5;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 这个是代理服务端
 */
public class ProxyServer {

    public static void main(String[] args) throws IOException {
        proxy4();
    }


    /**
     * 第四版  获取客户端传过来的真实ip地址+端口
     * @throws IOException
     */
    private static void proxy4() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10081));
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        //选择器
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //客户端的消息
        ByteBuffer read = ByteBuffer.allocate(1024);
        //要返回给客户端的消息
        ByteBuffer clientMsg = ByteBuffer.allocate(1024);

        while (true) {
            int readySize = selector.select();
            System.err.println(readySize + "个已经就绪");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            hasNext:
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    //拿到客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.err.println("接收到连接");
                    //注册到读取事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int length2;
                    retry:
                    while (true) {
                        read.clear();
                        int length = socketChannel.read(read);
                        if (length > 0) {
                            read.flip();
                            //读取到了客户端的消息，在这将客户端的消息转发给真实服务端
                            System.err.println(new String(read.array(), 0, length));
                            String[] proxy = Server.readLine(read);
                            String ip;
                            Integer port;
                            if (proxy.length == 0) {
                                System.out.println("没有获取到链接");
                                break hasNext;
                            } else if (proxy.length == 1) {
                                ip = proxy[0].trim();
                                port = 80;
                            }else{
                                ip = proxy[0].trim();
                                port = Integer.valueOf(proxy[1].trim());
                            }
                            read.flip();
                            SocketChannel server = SocketChannel.open();
                            server.connect(new InetSocketAddress(ip, port));
                            System.err.println("连接上真实服务器");
                            retry2:
                            while (true) {
                                if (server.finishConnect()) {
                                    server.write(ByteBuffer.wrap(read.array(), 0, length));
                                    System.err.println("消息转发给服务器");
                                    length2 = server.read(clientMsg);
                                    if (length2 > 0) {
                                        break retry2;
                                    }else{
                                        clientMsg.put("ok".getBytes());
                                        length2 = "ok".getBytes().length;
                                        break retry2;
                                    }
                                }
                            }
                            server.close();
                            break retry;
                            //System.err.println(new String(read.array(), 0, length));
                        }else{
//                            System.out.println("没有消息");
                        }
                    }
                    clientMsg.flip();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(clientMsg.array(), 0, length2);
                    clientMsg.rewind();
                    System.out.println(new String(clientMsg.array()));
                    socketChannel.write(byteBuffer);
                    System.err.println("消息回复完毕");
//                    socketChannel.register(selector, SelectionKey.OP_READ);
                    socketChannel.close();
                }
            }
        }
    }

    /**
     * 第三版，转发消息持续提供服务
     * @throws IOException
     */
    private static void proxy3() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10081));
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        //选择器
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //客户端的消息
        ByteBuffer read = ByteBuffer.allocate(1024);
        //要返回给客户端的消息
        ByteBuffer clientMsg = ByteBuffer.allocate(1024);

        while (true) {
            int readySize = selector.select();
            System.err.println(readySize + "个已经就绪");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    //拿到客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.err.println("接收到连接");
                    //注册到读取事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
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
                                        break retry2;
                                    }
                                }
                            }
                            server.close();
                            break retry;
                            //System.err.println(new String(read.array(), 0, length));
                        }else{
//                            System.out.println("没有消息");
                        }
                    }
                    clientMsg.flip();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(clientMsg.array(), 0, length2);
                    socketChannel.write(byteBuffer);
                    System.err.println("消息回复完毕");
//                    socketChannel.register(selector, SelectionKey.OP_READ);
                    socketChannel.close();
                }
            }
        }
    }

    /**
     * 第二版，实现接收到的消息转发功能
     * @throws IOException
     */
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
