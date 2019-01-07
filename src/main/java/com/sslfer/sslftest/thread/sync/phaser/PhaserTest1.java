package com.sslfer.sslftest.thread.sync.phaser;

import java.util.concurrent.Phaser;

/**
 * 多阶段栅栏测试一
 * 功能类似 CyclicBarrier
 *
 * @author sslf
 * @date 2019/1/7
 */
public class PhaserTest1 {


    public static void main(String[] args) {

        int count = 10;
        Phaser phaser = new Phaser();

        for (int i = 0; i < count; i++) {
            phaser.register();
            new Thread(new Task(phaser), "Thread-" + i).start();
        }

    }


    static class Task implements Runnable {

        private Phaser phaser;

        public Task(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            if (Thread.currentThread().getName().equals("Thread-0")) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 该方法响应中断
            int i = phaser.arriveAndAwaitAdvance();

            System.out.println(Thread.currentThread().getName() + ": 执行完任务，当前phase =" + i + "");
        }
    }

}
