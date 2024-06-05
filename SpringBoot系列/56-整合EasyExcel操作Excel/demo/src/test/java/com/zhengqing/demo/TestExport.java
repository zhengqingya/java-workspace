package com.zhengqing.demo;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 * <p> 测试导出 </p>
 *
 * @author zhengqingya
 * @description https://blog.csdn.net/qq_38623939/article/details/128845635
 * @date 2024/6/6 1:04
 */
public class TestExport {

    @Test
    public void test() throws Exception {
//        XSSFWorkbook(100000, 30); // java.lang.OutOfMemoryError: Java heap space 原因是excel文档的数据都存在内存中，当一直望excel填充数据，那么就越大，内存就扛不住
        SXSSFWorkbook(100000, 30); // SXSSF方式，用于超大数据量的操作
    }

    public static void XSSFWorkbook(int rows, int col) throws Exception {
        long start = System.currentTimeMillis();
        XSSFWorkbook workbook1 = new XSSFWorkbook();
        Sheet first = workbook1.createSheet("sheet1");
        for (int i = 0; i < rows; i++) {
            Row row = first.createRow(i);
            for (int j = 0; j < col; j++) {
                if (i == 0) {
                    // 首行
                    row.createCell(j).setCellValue("column" + j);
                } else {
                    // 数据
                    if (j == 0) {
                        CellUtil.createCell(row, j, String.valueOf(i));
                    } else
                        CellUtil.createCell(row, j, String.valueOf(Math.random()));
                }
            }
        }
        FileOutputStream out = new FileOutputStream("D:/test.xlsx");
        workbook1.write(out);
        out.close();
        System.out.println(("XSSFWorkbook " + (System.currentTimeMillis() - start) / 1000));
    }

    public static void SXSSFWorkbook(int rows, int col) throws Exception {
        long start = System.currentTimeMillis();
        XSSFWorkbook workbook1 = new XSSFWorkbook();
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook1, 100);
        Sheet first = sxssfWorkbook.createSheet("sheet1");
        for (int i = 0; i < rows; i++) {
            Row row = first.createRow(i);
            for (int j = 0; j < col; j++) {
                if (i == 0) {
                    // 首行
                    row.createCell(j).setCellValue("column" + j);
                } else {
                    // 数据
                    if (j == 0) {
                        CellUtil.createCell(row, j, String.valueOf(i));
                    } else
                        CellUtil.createCell(row, j, String.valueOf(Math.random()));
                }
            }
        }
        FileOutputStream out = new FileOutputStream("D:/test.xlsx");
        sxssfWorkbook.write(out);
        out.close();
        System.out.println(("SXSSFWorkbook " + (System.currentTimeMillis() - start) / 1000));
    }

}
