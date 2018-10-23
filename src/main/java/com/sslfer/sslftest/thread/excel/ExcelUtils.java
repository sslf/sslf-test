package com.sslfer.sslftest.thread.excel;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * @author sslf
 * @date 2018/10/23
 */
public class ExcelUtils {



    /**
     * 返回一个工作簿
     *
     * @param is 文件输入流
     * @return
     */
    public static Workbook getWorkbook(InputStream is) {
        Workbook targetExcel = null;

        if (!is.markSupported()) {
            is = new PushbackInputStream(is, 8);
        }

        try {
            if (POIFSFileSystem.hasPOIFSHeader(is)) {
                targetExcel = new HSSFWorkbook(is);
            } else if (POIXMLDocument.hasOOXMLHeader(is)) {
                targetExcel = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetExcel;
    }


}
