package com.geek45.exampleall.single;

import java.io.Serializable;

/**
 * 单例模式1
 * 懒汉式
 */
public class SingOne implements Serializable {
    //序列化id
    private static final long serialVersionUID = 1L;
    //私有构造函数
    private SingOne(){}
    //静态类，保证单例
    private static class SingleInner{
        private static SingOne test1 = new SingOne();
    }
    //获取单例对象
    public static SingOne getInstance(){
        return SingleInner.test1;
    }
    //反序列化，保证线程安全
    protected Object readResolve() {
        return SingleInner.test1;
    }
}
