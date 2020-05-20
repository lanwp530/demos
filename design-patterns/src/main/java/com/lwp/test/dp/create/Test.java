package com.lwp.test.dp.create;

/**
 * Test
 *
 * @author lanwenping
 * @version 1.0
 * @date 2020/5/17 13:20
 */
public class Test {
    private String field;
    public static String staticField ;
    public static void main(String[] args) {
        System.out.println(111);
        System.out.println(new Test().field);
        System.out.println(staticField);

        int i=0;
        for (int j = 0; j <10; j++) {
            System.out.println(j);
        }

        for (int j = 0; j <10; ++i) {
            System.out.println(j);
        }
    }
}
