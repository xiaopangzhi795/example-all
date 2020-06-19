package com.geek45.exampleall.io.nio.demo3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferDemo {

    private static String path = "D:\\Downloads\\REP-APP下单.log";


    /**
     * 读取文件 44.1MB  log文件
     * 读取内容 文件长度的1/10
     * 读取时长  平均80mm左右
     * 不加载内容时长：  3mm左右
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        long t1 = System.currentTimeMillis();
        testMethod();
        System.out.println(System.currentTimeMillis() - t1);
    }

    /**
     * 使用mappedByteBuffer进行文件读取
     */
    private static void testMethod() throws IOException {
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        aFile = new RandomAccessFile(path, "rw");
        fc = aFile.getChannel();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length());
        System.err.println((char)mbb.get((int)(aFile.length()/2-1)));
        System.err.println((char)mbb.get((int)(aFile.length()/2)));
        System.err.println((char)mbb.get((int)(aFile.length()/2)+1));
//        int length = (int) (aFile.length() / 10);
//        byte[] bytes = new byte[length];       //因为这个文件特别大，所以只读取出来这个文件的1/10
//        mbb.get(bytes);
//        System.err.println(new String(bytes, 0, length));
        fc.close();
        aFile.close();
    }

}
