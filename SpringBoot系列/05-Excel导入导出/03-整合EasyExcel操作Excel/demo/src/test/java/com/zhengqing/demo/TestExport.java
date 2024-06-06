package com.zhengqing.demo;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * <p> 测试导出 </p>
 *
 * @author zhengqingya
 * @description https://blog.csdn.net/qq_38623939/article/details/128845635
 * @date 2024/6/6 1:04
 */
public class TestExport {

    private int ID = 1;
    private int SUM = 1_0000;
    private int ONE_NUM = 1_000;

    @Test
    public void test() throws Exception {
//        XSSFWorkbook(100000, 30); // java.lang.OutOfMemoryError: Java heap space 原因是excel文档的数据都存在内存中，当一直望excel填充数据，那么就越大，内存就扛不住
        SXSSFWorkbook(SUM, 5); // SXSSF方式，用于超大数据量的操作
    }

    public void XSSFWorkbook(int rows, int col) throws Exception {
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

    public void SXSSFWorkbook(int rows, int col) throws Exception {
        long start = System.currentTimeMillis();
        XSSFWorkbook workbook1 = new XSSFWorkbook();
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook1, 100);
        Sheet sheet = sxssfWorkbook.createSheet("sheet1");
        Row row = sheet.createRow(0);
        for (int j = 0; j < col; j++) {
            row.createCell(j).setCellValue("column" + j); // 首行标题
        }

        long count = NumberUtil.ceilDiv(SUM, ONE_NUM);
        for (int i = 0; i < count; i++) {
            writeData(sheet, col);
        }

        FileOutputStream out = new FileOutputStream("D:/test.xlsx");
        sxssfWorkbook.write(out);
        out.close();
        System.out.println(("SXSSFWorkbook " + (System.currentTimeMillis() - start) / 1000));
    }

    private void writeData(Sheet sheet, int col) {
        List<Map<String, Object>> rowList = Lists.newArrayList();
        for (int i = 0; i < ONE_NUM; i++) {
            ID++;
            Row row = sheet.createRow(ID);

//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("id", ID);
//            map.put("name", RandomUtil.randomString("张三李四王五", 3));
//            map.put("time", DateUtil.now());
//            rowList.add(map);

            for (int j = 0; j < col; j++) {
                CellUtil.createCell(row, j, String.valueOf(i)); // 写数据
            }
        }
        int lastRowNum = sheet.getPhysicalNumberOfRows();
        System.out.println("文件行数:" + lastRowNum);
    }

}
