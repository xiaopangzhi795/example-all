package com.geek45.exampleall.io.nio.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Client {

    public static void main(String[] args) throws IOException {
        client();
    }

    private static void client() throws IOException {
        //打开链接
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //链接到服务端
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 10080));
        //打开选择器
        Selector selector = Selector.open();
        //将客户端注册到选择器上面
        socketChannel.register(selector, SelectionKey.OP_READ);

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {

            try {
                while (true) {
                    if (socketChannel.finishConnect()) {
                        System.out.println("链接成功");
                        System.out.println("输入消息");
                        String line = sc.nextLine();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
                        writeBuffer.put(line.getBytes());
                        writeBuffer.flip();
                        try {
                            socketChannel.write(writeBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("链接失败");
                        Thread.sleep(1000L);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }).start();

        while (true) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            int i = selector.select();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isReadable()) {
                    //读取事件
                    System.err.println("读取到消息");
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    readBuffer.clear();
                    int length = socketChannel1.read(readBuffer);
                    readBuffer.flip();
                    System.err.println(new String(readBuffer.array(), 0, length));
                    socketChannel1.register(selector, SelectionKey.OP_READ);
                }
            }
        }




    }

}
