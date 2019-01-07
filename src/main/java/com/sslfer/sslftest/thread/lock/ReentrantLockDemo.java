package com.sslfer.sslftest.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantLock 演示
 *
 * @author sslf
 * @date 2018/9/20
 */
public class ReentrantLockDemo {

    private ReentrantLock lock = new ReentrantLock(true);

    public void test() {

        lock.lock();

        try {
            System.out.println(Thread.currentThread().getName() + "：开始执行了");
        } finally {
            lock.unlock();
        }

    }


    public static void main(String[] args) throws InterruptedException {

        ReentrantLockDemo demo = new ReentrantLockDemo();

        Thread thread1 = new Thread(() -> {
            demo.test();
        }, "thread1");
        thread1.start();

        /*Thread thread2 = new Thread(() -> {
            demo.test();
        }, "thread2");
        thread2.start();

        Thread thread3 = new Thread(() -> {
            demo.test();
        }, "thread3");
        thread3.start();*/

        thread1.join();
        /*thread2.join();
        thread3.join();*/


        System.out.println("程序执行完成");
    }

}
