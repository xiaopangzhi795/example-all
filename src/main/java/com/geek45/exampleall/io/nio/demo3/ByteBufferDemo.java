package com.geek45.exampleall.io.nio.demo3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferDemo {

    private static String path = "D:\\Downloads\\REP-APP下单.log";

    /**
     * 读取文件 44.1MB  log文件
     * 读取内容 文件长度的1/10
     * 读取时长  平均130mm左右
     * 不加载内容时长：  35mm左右
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        long t1 = System.currentTimeMillis();
        methodTest();
        System.out.println(System.currentTimeMillis() - t1);
    }

    /**
     * ByteBuffer 方式读取文件
     */
    private static void methodTest() throws IOException {
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        aFile = new RandomAccessFile(path, "rw");
        fc = aFile.getChannel();
        long timeBegin = System.currentTimeMillis();
        ByteBuffer buff = ByteBuffer.allocate((int) aFile.length());
        buff.clear();
        fc.read(buff);
        System.out.println((char)buff.get((int)(aFile.length()/2-1)));
        System.out.println((char)buff.get((int)(aFile.length()/2)));
        System.out.println((char)buff.get((int)(aFile.length()/2)+1));
//        int length = (int) (aFile.length() / 10);
//        System.err.println(new String(buff.array(), 0, length));
        long timeEnd = System.currentTimeMillis();
        System.out.println("Read time: "+(timeEnd-timeBegin)+"ms");
        fc.close();
        aFile.close();
    }
}
