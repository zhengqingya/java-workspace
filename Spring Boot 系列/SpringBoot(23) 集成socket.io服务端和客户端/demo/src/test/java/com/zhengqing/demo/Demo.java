package com.zhengqing.demo;

/**
 *  <p>  测试demo </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/12/14$ 16:05$ <br/>
 * @version：  <br/>
 */
public class Demo {

    public static void main(String[] args) {
        Thread t = new Thread() {
            public void run() {
                pong();
            }
        };
        t.start();
        System.out.print("ping");
    }
    static void pong() {
        System.out.print("pong");
    }

}
