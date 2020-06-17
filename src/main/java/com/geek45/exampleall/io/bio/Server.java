package com.geek45.exampleall.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10085);
        System.out.println("开始监听10085端口");
        while (true) {
            System.out.println("服务器等待连接");
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                System.err.println(socket.getInetAddress());
                System.out.println(LocalDateTime.now() + "接收到连接");
                if (false) {
                    //发送
                    Commons.senderFile(socket);
                } else {
                    //接收
                    Commons.receive(socket);
                }
            }).start();
        }
    }


}
