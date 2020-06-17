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
     *
     *
     * RandomAccessFile  参数	意义
     * “r"	打开文件仅仅是为了读取数据，如果尝试调用任何写入数据的操作都会造成返回IOException错误信息的问题。
     * "rw"	打开文件用于读写两种操作，如果文件本身并不存在，则会创建一个全新的文件。
     * "rwd"	打开文件用于读写两种操作，这点和”rw“的操作完全一致，但是只会在cache满挥着调用RandomAccessFile.close()的时候才会执行内容同步操作。
     * "rws"	在"rwd"的基础上对内容同步的要求更加严苛，每write修改一个byte都会直接修改到磁盘中。
     *
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
