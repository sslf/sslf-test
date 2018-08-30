package com.sslfer.sslftest.thread.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch 测试
 * 5个人参加比赛。当5个人都准备好之后，才开始比赛；当5个人都通过终点后才算比赛结束
 *
 * @author sslf
 * @date 2018/8/30
 */
public class CountDownLatchDemo {

    // 参与比赛的人数
    private static int playerCount = 5;

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(playerCount);

        ExecutorService executorService = Executors.newFixedThreadPool(playerCount);

        for (int i = 0; i < playerCount; i++) {
            executorService.execute(new Player(i, begin, end));
        }

        System.out.println("裁判宣布：准备");

        Thread.sleep(1000L);

        // 主线程发开始指令
        begin.countDown();

        System.out.println("砰，比赛开始");

        // 等待比赛结束
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("比赛结束");
        }

        executorService.shutdown();
    }

}
