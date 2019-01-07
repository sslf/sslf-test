package com.sslfer.sslftest.thread.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环栅栏测试
 *
 * @author sslf
 * @date 2018/12/28
 */
public class CyclicBarrierTest {

    public static void main(String[] args) throws InterruptedException {

        int threadCount = 5;
        CountDownLatch placeRest = new CountDownLatch(threadCount);
        // 申明栅栏
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount, () -> {

            // 全部到达之后，执行的任务
            System.out.println("****** 所有运动员已准备完毕，发令枪：跑！******");
            // 任务执行完成之后，其他线程才能继续执行

        });

        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new Task(cyclicBarrier, placeRest), i + "");
            t.start();
        }

        placeRest.await();
        cyclicBarrier.reset();
    }

    static class Task implements Runnable {

        private CyclicBarrier cyclicBarrier;
        private CountDownLatch placeRest;

        public Task(CyclicBarrier cyclicBarrier, CountDownLatch placeRest) {
            this.cyclicBarrier = cyclicBarrier;
            this.placeRest = placeRest;
        }

        @Override
        public void run() {

            try {
                Thread.sleep(500);
                String name = Thread.currentThread().getName();
                System.out.println(name + ": 准备完成");
                // 在栅栏等待
                cyclicBarrier.await();
                Thread.sleep(500);
                System.out.println(name + ": 跑过了终点");
                placeRest.countDown();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }

}
