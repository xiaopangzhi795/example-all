package com.geek45.exampleall.io.nio;

import com.alibaba.fastjson.JSONObject;

import java.net.Socket;
import java.util.Scanner;

/**
 * NIO 客户端1
 */
public class ClientOne {

    public static void main(String[] args) {
        try {
            JSONObject object = new JSONObject();
            object.put("user", "qqq");
//            object.put("msg", "hello");
            Socket socket = new Socket("127.0.0.1", 10080);
            socket.setKeepAlive(true);
            new Thread(()->{
                try {
                    byte[] bytes = new byte[1024];
                    socket.getInputStream().read(bytes);
                    System.err.println(new String(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("输入msg");
                object.put("msg", scanner.nextLine());
                socket.getOutputStream().write(object.toJSONString().getBytes("utf-8"));
            }
//            socket.close();
        } catch (Exception e) {

        }
    }

}
