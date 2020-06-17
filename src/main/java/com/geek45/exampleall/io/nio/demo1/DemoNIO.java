package com.geek45.exampleall.io.nio.demo1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DemoNIO {
    private static String basePath = "D:\\";

    /**
     * NIO（这里通过RandomAccessFile进行操作，当然也可以通过FileInputStream.getChannel()进行操作）
     * @param args
     */
    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(basePath + "test.txt", "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            StringBuffer buffer = new StringBuffer();
            int length = 0;
            while ((length = fileChannel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                buffer.append(new String(byteBuffer.array(), 0, length,"utf-8"));
                byteBuffer.clear();
            }
            System.out.println(buffer.toString());
            fileChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.err.println(System.currentTimeMillis()-t1);
    }
}
