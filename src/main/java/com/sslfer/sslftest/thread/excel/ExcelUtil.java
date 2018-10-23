/*
package com.sslfer.sslftest.thread.excel;

import org.apache.commons.io.FileUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * excel 工具类 poi版本 3.9
 * 参考网站：http://www.tuicool.com/articles/emqaEf6
 * Created by sslf on 2016/8/17.
 *//*

public class ExcelUtil {

    private Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static final int TYPE_XLS = 1; // 2003
    public static final int TYPE_XLSX = 2; // 2007

    */
/**
     * 获取单元格的值
     *
     * @param cell
     * @return
     *//*

    private static Object getValue(Cell cell) {

        Object result;
        int cellType = cell.getCellType();

        if (cellType == Cell.CELL_TYPE_BOOLEAN) { // boolean
            result = cell.getBooleanCellValue();

        } else if (cellType == Cell.CELL_TYPE_NUMERIC) { // 数字

            // 如果是时间类型
            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {

                Date date = cell.getDateCellValue();
                result = DateUtil.date2String(date, "yyyy-MM-dd");

            } else {
                result = cell.getNumericCellValue();
            }


        } else { // 字符串
            result = cell.getStringCellValue();
        }

        return result;
    }


    */
/**
     * 读取excel文件
     *
     * @param path      文件路径
     * @param resultKey 返回Map的key,一一对应excel文件的列
     * @return
     * @throws IOException
     *//*

    public static List<Map<String, Object>> readExcel(String path, String[] resultKey) throws IOException {
        return readExcel(path, resultKey, 0);
    }


    */
/**
     * 获取一个excel表格实例
     *
     * @param type
     * @return
     *//*

    public static Workbook createWorkbook(int type) {
        Workbook targetExcel;

        if (type == TYPE_XLS) { // 2003
            targetExcel = new HSSFWorkbook();
        } else { // 2007
            targetExcel = new XSSFWorkbook();
        }

        return targetExcel;
    }

    public static Workbook createWorkbook(InputStream inputStream) {

        Workbook targetExcel = null;

        if (!inputStream.markSupported()) {
            inputStream = new PushbackInputStream(inputStream, 8);
        }

        try {
            if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
                targetExcel = new HSSFWorkbook(inputStream);
            } else if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
                targetExcel = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetExcel;

    }

    */
/**
     * 读取excel文件
     *
     * @param path      文件路径
     * @param resultKey 返回Map的key,一一对应excel文件的列
     * @param skipNum   跳过多少行开始取值
     * @return
     *//*

    public static List<Map<String, Object>> readExcel(String path, String[] resultKey, int skipNum) throws IOException {

        List<Map<String, Object>> result = new ArrayList<>();

        File excelFile = new File(path);
        FileInputStream fileInputStream = FileUtils.openInputStream(excelFile);

        Workbook targetExcel = createWorkbook(fileInputStream);

        // 读取Sheet
        for (int sheetIndex = 0; sheetIndex < targetExcel.getNumberOfSheets(); sheetIndex++) {

            Sheet sheet = targetExcel.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }

            // 循环获取行row

            for (int rowIndex = 0 + skipNum; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                // 获取列的值，组装成map
                Map<String, Object> rowMap = new HashMap<>();
                int flag = 0;
                for (int i = 0; i < resultKey.length; i++) {
                    Cell cell = row.getCell(i);

                    if (cell != null) {
                        Object value = getValue(cell);
                        if (CommonUtil.isNotEmpty(value)) {
                            flag += 1;
                        }
                        rowMap.put(resultKey[i], value);
                    }
                }
                if (flag > 0) {
                    result.add(rowMap);
                }
            }
        }

        return result;
    }


    */
/**
     * 读取excel文件
     *
     * @param path    文件路径
     * @param skipNum 跳过多少行开始取值
     * @return
     *//*

    public static List<Map<String, Object>> readExcel(String path, int skipNum) throws IOException {

        List<Map<String, Object>> result = new ArrayList<>();

        File excelFile = new File(path);
        FileInputStream fileInputStream = FileUtils.openInputStream(excelFile);

        Workbook targetExcel = createWorkbook(fileInputStream);

        // 读取Sheet
        for (int sheetIndex = 0; sheetIndex < targetExcel.getNumberOfSheets(); sheetIndex++) {

            Sheet sheet = targetExcel.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }

            //存放列名
            List<String> cellKeys = new ArrayList<>();

            Row row0 = sheet.getRow(0);
            int coloumNum = row0.getPhysicalNumberOfCells();

            for (int i = 0; i < coloumNum; i++) {

                Cell cell = row0.getCell(i);

                if (cell != null) {
                    Object value = getValue(cell);
                    cellKeys.add(value.toString());
                }

            }

            // 循环获取行row

            for (int rowIndex = 0 + skipNum; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                // 获取列的值，组装成map
                Map<String, Object> rowMap = new HashMap<>();
                int flag = 0;
                for (int i = 0; i < cellKeys.size(); i++) {
                    Cell cell = row.getCell(i);

                    if (cell != null) {
                        Object value = getValue(cell);
                        if (CommonUtil.isNotEmpty(value)) {
                            flag += 1;
                        }
                        rowMap.put(cellKeys.get(i), value);
                    }
                }
                if (flag > 0) {
                    result.add(rowMap);
                }
            }
        }

        return result;
    }


    */
/**
     * 获取workbook
     *
     * @param type  要获取的类型
     * @param param 填充的数据 key为Sheet的名称，如果为null则表示不需要名称
     * @return 填充好数据的workbook
     *//*

    private static Workbook getWorkbook(int type, Map<String, List<Map<String, Object>>> param) {

        Workbook targetExcel = createWorkbook(type);

        for (String sheetName : param.keySet()) {

            List<Map<String, Object>> data = param.get(sheetName);

            Sheet sheet = targetExcel.createSheet(sheetName);


            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i);
                sheet.autoSizeColumn((short) i);

                Map<String, Object> cellData = data.get(i);

                int j = 0;
                for (String key : cellData.keySet()) {

                    Cell cell = row.createCell(j);

                    Object cellValue = cellData.get(key);

                    if (cellValue != null) {
                        String dataStr = cellValue.toString();
                        cell.setCellValue(dataStr);
//                    int len = dataStr.length() + getChineseNum(dataStr);
                        int len = dataStr.getBytes().length * 2 * 256;
                        if (len < 1200) {
                            len = 1200;
                        }
                        sheet.setColumnWidth(j, len);
                    }
                    j++;

                }
            }
        }

        return targetExcel;
    }

    public static int getChineseNum(String context) {
        ///统计context中是汉字的个数
        int lenOfChinese = 0;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        //汉字的Unicode编码范围
        Matcher m = p.matcher(context);
        while (m.find()) {
            lenOfChinese++;
        }
        return lenOfChinese;
    }

    */
/**
     * 方法重载，style为要传进来的表格样式
     *
     * @param type
     * @param param
     * @return
     *//*

    private static Workbook getWorkbook(int width, int heigth, int type, Map<String, List<Map<String, Object>>> param) {
        Workbook targetExcel = createWorkbook(type);

        for (String sheetName : param.keySet()) {

            List<Map<String, Object>> data = param.get(sheetName);

            Sheet sheet = targetExcel.createSheet(sheetName);

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i);
                //高度
                row.setHeightInPoints(heigth);

                Map<String, Object> cellData = data.get(i);

                int j = 0;
                for (String key : cellData.keySet()) {
                    row.createCell(j).setCellValue(cellData.get(key).toString());
                    j++;
                }
            }
            int numberOfCells = sheet.getRow(0).getPhysicalNumberOfCells();
            for (int i = 0; i < numberOfCells; i++) {
                //宽度
                sheet.setColumnWidth((short) i, width);
            }

        }

        return targetExcel;
    }

    */
/**
     * 生成简单的excel
     *
     * @param type 版本，xls xlsx
     * @param path excel保存路径
     * @param data 写入excel中的数据，可以包含头部
     *//*

    public static void writeExcel(int type, String path, List<Map<String, Object>> data) throws IOException {

        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put(null, data);

        Workbook targetExcel = getWorkbook(type, param);

        FileOutputStream out = new FileOutputStream(path);
        targetExcel.write(out);

        out.close();
    }


    */
/**
     * 生成简单的excel
     *
     * @param type  版本，xls xlsx
     * @param path  excel保存路径
     * @param param 写入excel中的数据，可以包含头部
     *//*

    public static void writeExcel(int type, String path, Map<String, List<Map<String, Object>>> param) throws IOException {

        Workbook targetExcel = getWorkbook(type, param);

        FileOutputStream out = new FileOutputStream(path);
        targetExcel.write(out);

        out.close();
    }


    */
/**
     * 获取生成excel的inputStream
     *
     * @param type  版本
     * @param param 写入excel中的数据，可以包含头部
     * @return 生成的excel InputStream，调用完成时应该关闭inputStream
     *//*

    public static InputStream getExcel(int type, LinkedHashMap<String, List<Map<String, Object>>> param) throws IOException {

        ByteArrayOutputStream byteOutPut = new ByteArrayOutputStream();

        Workbook workbook = getWorkbook(type, param);
        workbook.write(byteOutPut);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteOutPut.toByteArray());

        return byteArrayInputStream;
    }

    */
/**
     * @param width  表格的宽
     * @param height 表格的高
     * @param type   excel的类型
     * @param param  要封装的数据
     * @return
     * @throws IOException
     *//*

    public static InputStream getExcel(int width, int height, int type, LinkedHashMap<String, List<Map<String, Object>>> param) throws IOException {
        ByteArrayOutputStream byteOutPut = new ByteArrayOutputStream();
        Workbook workbook = getWorkbook(width, height, type, param);
        workbook.write(byteOutPut);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteOutPut.toByteArray());
        return byteArrayInputStream;
    }


    public static void main(String[] args) {

        // String path = "F:\\中文名称测试表格.xlsx";

        // String path = "D:/work/测试.xlsx";
        String path = "D:/work/养老险团体清单.xls";

        try {
            */
/*List<Map<String, Object>> list = ExcelUtil.readExcel(path, new String[]{"key1", "key2", "key3"},1);


            for (Map<String, Object> stringObjectMap : list) {
                System.out.println(stringObjectMap);
            }*//*


            */
/*List<Map<String,Object>> data = new ArrayList<>();

            Map<String,Object> head = new HashMap<>();
            head.put("aa","姓名");
            head.put("bb","性别");
            data.add(head);

            Map<String,Object> row1 = new HashMap<>();
            row1.put("aa","张三");
            row1.put("bb","男");
            data.add(row1);

            ExcelUtil.writeExcel(ExcelUtil.TYPE_XLS,"F:\\写入中文名称测试表格.xls",data);*//*


            List<Map<String, Object>> list = ExcelUtil.readExcel(path, new String[]{"key1", "key2", "key3", "key4", "key5", "key6", "key7", "key8", "key9", "key10", "key11"}, 783);
            for (Map<String, Object> stringObjectMap : list) {
                System.out.println(stringObjectMap);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}*/
