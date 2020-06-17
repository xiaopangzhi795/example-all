package com.geek45.exampleall.io.nio.demo1;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoIO {

    private static String basePath = "D:\\";

    /**
     * 采用FileInputStream读取文件内容
     * @param args
     */
    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        InputStream inputStream = null;
        File file = new File(basePath + "test.txt");
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            StringBuffer buffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            System.out.println(buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println(System.currentTimeMillis() - t1);
    }
}
