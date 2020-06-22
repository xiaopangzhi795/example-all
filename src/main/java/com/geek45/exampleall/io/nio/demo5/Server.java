package com.geek45.exampleall.io.nio.demo5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/***
 *  这个当做真实的服务端
 */
public class Server {

    public static void main(String[] args) throws IOException {
//        server2();

        String ss = "CONNECT csi.gstatic.com:443 HTTP/1.1\n" +
                "Host: csi.gstatic.com:443\n" +
                "Proxy-Connection: keep-alive\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36\n" +
                "\n";
        ByteBuffer byteBuffer = ByteBuffer.wrap(ss.getBytes());
        readLine(byteBuffer);

    }

    /**
     * 按行读取byteBuffer
     * @param byteBuffer
     */
    public static String[] readLine(ByteBuffer byteBuffer) {
        byteBuffer.rewind();
        int last = byteBuffer.limit() - 1;
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data, 0, last);
        byteBuffer.flip();
        int i = last;
        while (data[i] != (byte) '\n') {
            i--;
            if (i < 0) {
                break;
            }
        }

        if (i > 0) {
            String str = new String(data, 0, data.length);
            if (str.endsWith("\n\n")) {
                //最后一行
                String[] strs = str.split("\n");
                for (String ss : strs
                ) {
                    System.err.println(ss);
                }
            }else{
                str = new String(data, 0, i + 1);
                String[] strs = str.split("\n");
                for (String ss : strs
                ) {
                    System.err.println(ss);
                    if (ss.startsWith("Host")) {
                        String[] proxy = ss.split(" ");
                        for (String pro : proxy
                        ) {
                            if (!pro.contains("Host")) {
                                if (pro.contains(":")) {
                                    if (pro.contains(":")) {
                                        return pro.split(":");
                                    }
                                }else{
                                    return new String[]{pro};
                                }
                            }

                        }
                    }
                }
            }
        }else{
            System.err.println(new String(data, 0, data.length));
        }
        return new String[0];
    }

    /**
     * 第二版，循环提供服务
     * @throws IOException
     */
    private static void server2() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10082));
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        //选择器
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer read = ByteBuffer.allocate(1024);
        ByteBuffer byteBuffer = ByteBuffer.wrap("收到".getBytes());
        while (true) {
            int readySize = selector.select();
            System.err.println(readySize + "个已经就绪");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.err.println("接收到连接");
                    //将链接注册到读取事件
                    socketChannel.register(selector, SelectionKey.OP_READ);


                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    retry:
                    while (true) {
                        read.clear();
                        int length = socketChannel.read(read);
                        if (length > 0) {
                            read.flip();
                            System.err.println(new String(read.array(), 0, length));
                            break retry;
                        }else{
//                            System.out.println("没有消息");
                        }
                    }
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    byteBuffer.rewind();
                    socketChannel.write(byteBuffer);
                    System.err.println("消息回复完毕");
                    socketChannel.close();
                }
            }
        }


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
