package com.geek45.exampleall.io.nio.demo4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * UDP 链接服务端
 */
public class Server {

    public static void main(String[] args) throws IOException {
        server2();
    }

    /**
     * UDP服务端第二种实现方式
     */
    private static void server2() throws IOException {

        DatagramSocket datagramSocket = new DatagramSocket(10080);

        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, 15);

        Selector selector = Selector.open();

        DatagramChannel datagramChannel = datagramSocket.getChannel();
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isReadable()) {
                    datagramSocket.receive(datagramPacket);
                    System.err.println(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
                    datagramChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        }




    }

    /**
     * UDP服务端第一种实现方式
     * @throws IOException
     */
    private static void server() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(10080));
        datagramChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isReadable()) {
                    byteBuffer.clear();
                    datagramChannel.receive(byteBuffer);
                    byteBuffer.flip();
                    System.err.println(new String(byteBuffer.array()));
                    datagramChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        }

    }

}
