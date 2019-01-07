package com.sslfer.sslftest.thread.sync.phaser;

import java.util.concurrent.Phaser;

/**
 * 多阶段栅栏测试三
 * 控制任务轮数
 *
 * @author sslf
 * @date 2019/1/7
 */
public class PhaserTest3 {


    public static void main(String[] args) {

        // 10个线程
        int count = 10;
        // 3个阶段
        int repeats = 3;

        Phaser phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {

                System.out.println("---------------PHASE[" + phase + "],Parties[" + registeredParties + "] ---------------");

                return phase + 1 >= repeats || super.onAdvance(phase, registeredParties);
            }
        };

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

            // 只要栅栏没有终止，就一直执行
            while (!phaser.isTerminated()) {

                int i = phaser.arriveAndAwaitAdvance();

                System.out.println(Thread.currentThread().getName() + ": 执行完任务，当前phase =" + i + "");
            }
        }
    }

}
