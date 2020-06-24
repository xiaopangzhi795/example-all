package com.geek45.exampleall.single;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 单例模式测试
 */
public class SingleTest {
    public static void main(String[] args) {

        try {
            //懒汉式
            SingOne test1 = SingOne.getInstance();
            System.out.println(test1.hashCode());
            //序列化
            FileOutputStream fo = new FileOutputStream("tem");
            ObjectOutputStream oo = new ObjectOutputStream(fo);
            oo.writeObject(test1);
            oo.close();
            fo.close();
            //反序列化
            FileInputStream fi = new FileInputStream("tem");
            ObjectInputStream oi = new ObjectInputStream(fi);
            SingOne test2 = (SingOne) oi.readObject();
            oi.close();
            fi.close();
            System.out.println(test2.hashCode());

            //饿汉式
//            SingTwo test1 = SingTwo.getInstance();
//            System.out.println(test1.hashCode());
//            //序列化
//            FileOutputStream fo = new FileOutputStream("tem2");
//            ObjectOutputStream oo = new ObjectOutputStream(fo);
//            oo.writeObject(test1);
//            oo.close();
//            fo.close();
//            //反序列化
//            FileInputStream fi = new FileInputStream("tem2");
//            ObjectInputStream oi = new ObjectInputStream(fi);
//            SingTwo test2 = (SingTwo) oi.readObject();
//            oi.close();
//            fi.close();
//            System.out.println(test2.hashCode());
        } catch (Exception e) {

        }
    }
}
