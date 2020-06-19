package com.geek45.exampleall.io.nio.demo4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * UDP 客户端
 */
public class Client {

    public static void main(String[] args) throws IOException {
        client();
    }

    /**
     * 分段发送大文件，不在意文件的完整性
     */
    private static void client2(){

    }

    /**
     * 基础的UPD客户端
     * @throws IOException
     */
    private static void client() throws IOException {
        DatagramChannel channel = null;
        channel = DatagramChannel.open();
        String info = "I'm the Sender!";
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.clear();
        buf.put(info.getBytes());
        buf.flip();
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 10080);
        int bytesSent = channel.send(buf, socketAddress);
        System.out.println(bytesSent);
        channel.close();
    }

}
