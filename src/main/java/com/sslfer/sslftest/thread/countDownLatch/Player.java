package com.sslfer.sslftest.thread.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * 参与比赛的人
 *
 * @author sslf
 * @date 2018/8/30
 */
public class Player implements Runnable {

    Integer id;
    CountDownLatch start;
    CountDownLatch end;

    public Player(Integer id, CountDownLatch start, CountDownLatch end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {

        // 等待开始执行指令
        try {
            start.await();

            System.out.println(System.currentTimeMillis() + ":选手" + id + "出发");

            // 比赛中
            Thread.sleep((long) (Math.random() * 100) * 100);

            System.out.println("选手id：" + id + " 到达终点");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            end.countDown();
        }

    }

}
