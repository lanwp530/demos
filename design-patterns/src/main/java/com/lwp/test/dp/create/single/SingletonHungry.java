package com.lwp.test.dp.create.single;

/**
 * 饿汉式单例
 * <p>
 *     以空间换时间
 * </p>
 *
 * @author lanwenping
 * @version 1.0
 * @date 2020/5/16 13:33
 */
public class SingletonHungry {
    public static volatile SingletonHungry instance = new SingletonHungry();

    private SingletonHungry() {}

    public static SingletonHungry getInstance() {
        return instance;
    }
}
