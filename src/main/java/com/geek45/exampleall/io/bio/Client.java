package com.geek45.exampleall.io.bio;

import com.alibaba.fastjson.JSONObject;
import com.geek45.commons.test.bio.BioTest;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Bio 的客户端
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 10085);
        System.err.println(LocalDateTime.now() + "连接成功");
        socket.setKeepAlive(true);
        new Thread(() -> Commons.receiveForClient(socket)).start();

        Scanner sc = new Scanner(System.in);
        JSONObject object = new JSONObject();
        object.put("user", "老钱");
        while (true) {
            System.err.println("请输入");
            String msg = sc.nextLine();
            object.put("msg", msg);
            BioTest.sender(socket, object.toJSONString());
        }

    }

}
