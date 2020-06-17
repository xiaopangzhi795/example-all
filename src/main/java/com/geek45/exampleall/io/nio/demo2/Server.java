package com.geek45.exampleall.io.nio.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static void main(String[] args) throws IOException {
        server();
    }


    /**
     * NIO的形式，注册客户端到链接事件上
     *
     * 有客户端连接后，将该链接注册到读取的事件上面，等待用户的消息
     *
     * 读取到消息后，注册到写事件上面，告诉客户端收到了。
     *
     * 然后再将该链接注册到读取上面，持续接收消息
     *
     * @throws IOException
     */
    private static void server() throws IOException {
        //打开
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //绑定
        socketChannel.bind(new InetSocketAddress(10080));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //打开选择器
        Selector selector = Selector.open();
        //注册channel  指定感兴趣的事情时accept
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // buffer 字节流
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        ByteBuffer writeBuffer = ByteBuffer.allocate(20);
        writeBuffer.put("收到".getBytes());

        while (true) {
            int nReady = selector.select();   // 此处会阻塞，直到至少有一个注册的事件已经就绪
            System.out.println(nReady);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();      // 将所有就绪事件拿出
            Iterator<SelectionKey> it = selectionKeys.iterator();       //生成Iterator

            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();          //拿出来一个SelectionKey
                it.remove();                                    //将拿出来的SelectionKey 删除掉

                if (selectionKey.isAcceptable()) {
                    //如果是有客户端连接过来
                    //将该链接注册到读取或者写入
                    SocketChannel channel = socketChannel.accept();     //接收链接
                    channel.configureBlocking(false);           // 连接设置为非阻塞
                    channel.register(selector, SelectionKey.OP_READ);   //将该连接注册到读取事件
                    System.err.println("该链接已经注册到读取事件");
                } else if (selectionKey.isReadable()) {
                    // 如果是有客户端写入消息
                    //将消息读取出来并打印
                    System.err.println("接收到客户端的消息");
                    SocketChannel channel = (SocketChannel) selectionKey.channel();     //获取链接客户端对象
                    readBuffer.clear();     //清空缓冲区
                    int length;   //将内容读取到缓冲区
                    StringBuffer buffer = new StringBuffer();
                    while ((length = channel.read(readBuffer)) > 0) {
                        readBuffer.flip();      //指针挪回去
                        buffer.append(new String(readBuffer.array(), 0, length, "utf-8"));      //将读取出来的数据变成字符串
                    }
                    System.err.println(buffer.toString());
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                    System.err.println("客户端消息读取完成，注册到写的事件上");
                } else if (selectionKey.isWritable()) {
                    System.out.println("开始发送消息");
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    writeBuffer.rewind();
                    channel.write(writeBuffer);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("消息发送完毕，将该连接重新注册到了读取上面");
                }
            }
        }

    }
}
