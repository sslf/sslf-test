package com.sslfer.sslftest.thread.excel;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

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
     * 下一次读取索引
     */
    private volatile int index = 0;
    /**
     * 本页最大的行号
     */
    private int lastRowNum = 0;
    /**
     * 重入锁
     */
    private final ReentrantLock lock = new ReentrantLock();
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 同步获取要读取的行号
     *
     * @return
     */
    public int getIndex() {

        lock.lock();
        try {

            index += 1;
            System.out.println("index:" + index);
            return index;
        } finally {
            lock.unlock();
        }

    }

    /**
     * 读取excel
     *
     * @param workbook
     */
    public void read(Workbook workbook) {

        // 现在默认是第一个 sheet
        Sheet sheet = workbook.getSheetAt(0);
        this.lastRowNum = sheet.getLastRowNum();
        System.out.println("lastRowNum:" + lastRowNum);

        countDownLatch.countDown();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

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

                // 添加到queue中
                this.queue.put(info);

                return info;
            });
        }

        // 慢慢关闭
        executorService.shutdown();
        System.out.println("关闭了");

    }

    private ExcelInfo getInfo(Row row) {

        Cell cell = row.getCell(2);
        String name = cell.getStringCellValue();

        ExcelInfo excelInfo = new ExcelInfo();
        excelInfo.setName(name);

        return excelInfo;
    }

    /**
     * 重队列中获取
     *
     * @return
     */
    public ExcelInfo getInfo() throws InterruptedException {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(queue.size());


        ExecutorService executorService = Executors.newFixedThreadPool(2);


        ExcelInfo take = this.queue.take();

        return take;
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        // 读取excel

        String path = Main.class.getClassLoader().getResource("").getPath() + "temp.xls";
        System.out.println(path);
        File excelFile = new File(path);
        FileInputStream fis = FileUtils.openInputStream(excelFile);

        Workbook workbook = ExcelUtils.getWorkbook(fis);

        Main main = new Main();

        // 多线程读取
        main.read(workbook);

        // 多线程消费
        ExcelInfo excelInfo = main.queue.take();
        System.out.println(excelInfo.toString());
    }

}
