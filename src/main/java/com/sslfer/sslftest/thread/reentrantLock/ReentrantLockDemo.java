package com.sslfer.sslftest.thread.reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantLock 演示
 *
 * @author sslf
 * @date 2018/9/20
 */
public class ReentrantLockDemo {

    private ReentrantLock lock = new ReentrantLock(true);
    private Condition condition = lock.newCondition();

    private int count = 0;




    public static void main(String[] args) throws InterruptedException {

        ReentrantLockDemo demo = new ReentrantLockDemo();

        for (int i = 0; i < 3; i++) {


        }

        Thread.sleep(3000);

        System.out.println(demo.count);
    }

}
