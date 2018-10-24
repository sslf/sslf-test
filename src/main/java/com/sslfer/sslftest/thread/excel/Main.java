package com.sslfer.sslftest.thread.excel;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 多线程读取excel到queue中，然后都多线程打印或者写入到新的文件中
 * 读取时候的要求：2个或者3个线程交替读取，放到blokingQueue中
 *
 * @author sslf
 * @date 2018/10/23
 */
public class Main {

    /**
     * 申明一个容量是1000的queue
     */
    ArrayBlockingQueue<ExcelInfo> queue = new ArrayBlockingQueue(1000);
    /**
     * 本页最大的行号
     */
    private int lastRowNum = 0;

    private CountDownLatch countDownLatch = new CountDownLatch(1);


    /**
     * 读取excel
     *
     * @param workbook
     */
    public void read(Workbook workbook, int threadNum) {

        // 现在默认是第一个 sheet
        Sheet sheet = workbook.getSheetAt(0);
        this.lastRowNum = sheet.getLastRowNum();
        System.out.println("lastRowNum:" + lastRowNum);

        countDownLatch.countDown();

        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        for (int i = 0; i < lastRowNum; i++) {
            int finalI = i;
            Future<ExcelInfo> submit = executorService.submit((Callable<ExcelInfo>) () -> {

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 读取开始
                // int index = this.getIndex();
                Row row = sheet.getRow(finalI);

                ExcelInfo info = getInfo(row);
                Thread.sleep(500);
                System.out.println("生产：" + info.toString());
                // 添加到queue中
                this.queue.put(info);

                return info;
            });
        }

        // 慢慢关闭
        executorService.shutdown();
        System.out.println("关闭了");

    }

    private ExcelInfo getInfo(Row row) throws InterruptedException {

        Cell cell = row.getCell(2);
        String name = cell.getStringCellValue();

        ExcelInfo excelInfo = new ExcelInfo();
        excelInfo.setName(name);
        System.out.println(queue.size());

        return excelInfo;
    }

    /**
     * 重队列中获取
     *
     * @return
     */
    public void take(Integer threadNum) {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        for (int i = 0; i < lastRowNum; i++) {

            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {

                    try {

                        // 随机 50 到 2000 的毫秒
                        int min = 50;
                        int max = 2000;
                        Random random = new Random();

                        int time = random.nextInt(max) % (max - min + 1) + min;

                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 获取队列
                    ExcelInfo take = null;
                    try {
                        take = queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("消费：" + take.toString());

                    return null;
                }
            });
        }

        executorService.shutdown();

    }


    public static void main(String[] args) throws IOException, InterruptedException {

        // 读取excel

        String path = Main.class.getClassLoader().getResource("").getPath() + "temp.xls";

        File excelFile = new File(path);
        FileInputStream fis = FileUtils.openInputStream(excelFile);

        Workbook workbook = ExcelUtils.getWorkbook(fis);

        Main main = new Main();

        // 多线程读取
        main.read(workbook, 8);

        // 多线程消费
        main.take(2);
    }

}
