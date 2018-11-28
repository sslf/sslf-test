package com.sslfer.sslftest.thread.Singleton;

/**
 * 单例实现方式1
 *
 * @author sslf
 * @date 2018/11/15
 */
public class Singleton1 {

    private static volatile Singleton1 bean = null;

    private Singleton1() {
    }

    private static Singleton1 getInstance(){

        if (bean == null) {
            synchronized (Singleton1.class){
                if (bean == null) {
                    bean = new Singleton1();
                }
            }
        }

        return bean;

    }

    public static void main(String[] args) {

        System.out.println(1);

        Singleton1 temp = new Singleton1();
        try {
            temp.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(2);

    }

}
