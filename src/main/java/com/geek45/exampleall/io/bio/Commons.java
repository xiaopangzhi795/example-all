package com.geek45.exampleall.io.bio;

import com.alibaba.fastjson.JSONObject;
import com.geek45.commons.util.proxy.commons.SessionIDGenerate;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Commons {

    private static String basePath = "D:\\Downloads\\test\\";

    //socket 缓存
    private static Map<String, Socket> socketMap = new ConcurrentHashMap<>();

    protected static void put(String sessionId, Socket socket) {
        socketMap.put(sessionId, socket);
    }

    /**
     * 简单接收字符串
     *
     * @param socket
     */
    public static void receive(Socket socket) {
        byte[] buffer = new byte[1024];
        try {
            while (true) {
                socket.getInputStream().read(buffer);
                String msg = new String(buffer);
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单接收字符串
     *
     * @param socket
     */
    public static void receiveForClient(Socket socket) {
        byte[] buffer = new byte[1024];
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = bufferedReader.readLine();
            while (true) {
                System.err.println(msg);
                System.out.println("等待接收数据");
                socket.getInputStream().read(buffer);
                msg = bufferedReader.readLine();
                System.out.println("接收到数据" + new String(buffer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收文件
     * @param socket
     */
    public static void receiveFile(Socket socket) {
        System.err.println("开始接收文件");
        File directory = new File(basePath + SessionIDGenerate.getInstance().generateId());
        DataInputStream dataInputStream = null;
        FileOutputStream fos = null;
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            String fileName = dataInputStream.readUTF();
            System.err.println(LocalDateTime.now() + "文件名接收成功");
            long fileLenght = dataInputStream.readLong();
            System.err.println(LocalDateTime.now() + "文件长度接收成功");
            File file = new File(directory.getPath() + File.separatorChar + fileName);
            byte[] bytes = new byte[1024];
            fos = new FileOutputStream(file);
            //开始传输文件
            int length = 0;
            int progress = 0;
            while ((length = dataInputStream.read(bytes, 0, bytes.length)) != -1) {
                progress += length;
                fos.write(bytes, 0, length);
                fos.flush();
                System.err.println(LocalDateTime.now() + "文件接收进度：" + (100 * progress / fileLenght) + "%");
            }
            System.err.println(LocalDateTime.now() + "文件接收完成");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 简单发送字符串
     *
     * @param socket
     */
    public static void sender(Socket socket, String msg) {
        try {
            System.out.println("开始发送数据");
            socket.getOutputStream().write(msg.getBytes());
            System.err.println("发送数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件
     * @param socket
     */
    public static void senderFile(Socket socket) {
        System.err.println("准备传输文件");
        File file = new File("D:\\Downloads\\tale.db");
        FileInputStream fileInputStream = null;
        DataOutputStream dos = null;
        try {
            if (file.exists()) {
                System.err.println("文件存在，开始传输");
                fileInputStream = new FileInputStream(file);
                dos = new DataOutputStream(socket.getOutputStream());
                //文件名
                dos.writeUTF(file.getName());
                dos.flush();
                System.err.println(LocalDateTime.now() + "文件名传输完成");
                //文件长度
                dos.writeLong(file.length());
                dos.flush();
                System.err.println(LocalDateTime.now() + "文件长度传输完成");
                //开始传输文件
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes);
                    dos.flush();
                    progress += length;
                    System.err.println(LocalDateTime.now() + "文件传输进度:" + (100 * progress / file.length()) + "%");
                }
                System.out.println(LocalDateTime.now() + "文件传输完成");
                //传输OK
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
