package com.sslfer.sslftest.thread.sync.phaser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Phaser;

/**
 * 多阶段栅栏测试二
 * 类似 countdownlatch
 *
 * @author sslf
 * @date 2019/1/7
 */
public class PhaserTest2 {


    public static void main(String[] args) throws IOException {

        int count = 10;
        Phaser phaser = new Phaser(1);

        for (int i = 0; i < count; i++) {
            phaser.register();
            new Thread(new Task(phaser), "Thread-" + i).start();
        }

        // 开关的设置
        // 外部条件:等待用户输入命令
        System.out.println("随便输入，开始执行");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        // 打开开关
        phaser.arriveAndDeregister();
        System.out.println("主线程打开了开关");

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
