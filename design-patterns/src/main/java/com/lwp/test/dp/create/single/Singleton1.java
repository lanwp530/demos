package com.lwp.test.dp.create.single;

/**
 * 懒汉式单例
 *
 * @author lanwenping
 * @version 1.0
 * @date 2020/5/16 13:33
 */
public class Singleton1 {

//    public static volatile Singleton1 instance;
    public static  Singleton1 instance;

    private Singleton1() {}

    /**
     * 线程不安全获取单例
     * @return
     */
    public static Singleton1 getInstance() {
        // 线程不安全
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }

    /**
     * 线程安全获取单例(不推荐)获取单例 synchronized 性能低
     * @return
     */
    public static synchronized Singleton1 getInstance1() {
        // 线程不安全
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }

    /**
     * 线程安全获取单例
     * @return
     */
    public static Singleton1 getInstance2() {
        if (instance == null) {
            // 使用 synchronized 和双重检查保证线程安全
            synchronized (Singleton1.class) {
                if (instance == null) {
                    instance = new Singleton1();
                }
            }
        }
        return instance;
    }
}
