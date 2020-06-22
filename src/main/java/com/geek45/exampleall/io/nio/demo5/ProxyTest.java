package com.geek45.exampleall.io.nio.demo5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 *  过于复杂，时间紧急，后续补充----------
 *
 */
public class ProxyTest {

    static ByteBuffer local2Remote = ByteBuffer.allocate(1024);
    static ByteBuffer remote2Local = ByteBuffer.allocate(1024);
    static SocketChannel local = null;
    static SocketChannel remote = null;



    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        local = serverSocketChannel.accept();



        //发送给服务端

        //读取服务端的消息

        //返回给客户端

    }

    private static void localSocket(){
        while (true) {
            if (local != null) {

            }else{
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
