package com.lwp.test.dp.create.single;

/**
 * InnerClassSingle
 *  静态内部类
 * @author lanwenping
 * @version 1.0
 * @date 2020/5/16 13:44
 */
public class InnerClassSingle {
    private static class SingletonHolder {
        private static final InnerClassSingle INSTANCE = new InnerClassSingle();
    }
    private InnerClassSingle (){}
    public static final InnerClassSingle getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
