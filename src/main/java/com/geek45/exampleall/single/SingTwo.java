package com.geek45.exampleall.single;

import java.io.Serializable;

/**
 * 单例模式2
 * 饿汉式
 */
public class SingTwo implements Serializable {
    //序列化id
    private static final long serialVersionUID = 2L;
    //私有化构造函数
    private SingTwo(){}
    //单例对象
    private static SingTwo test2;
    //静态方法区，实例化
    static {
        test2 = new SingTwo();
    }
    //获取单例对象
    public static SingTwo getInstance(){
        return test2;
    }
    //反序列化
    protected Object readResolve(){
        return test2;
    }

}
