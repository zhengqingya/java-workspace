package com.zhengqing.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p> 测试导出 </p>
 *
 * @author zhengqingya
 * @description https://hutool.cn/docs/#/poi/Excel%E5%A4%A7%E6%95%B0%E6%8D%AE%E7%94%9F%E6%88%90-BigExcelWriter
 * 注意：大数据量 ExcelWriter会OOM getBigWriter可解决OOM
 * @date 2024/6/6 1:15
 */
@Slf4j
public class TestExportHutool {

    private long ID = 0; // 记录总数
    private int SUM = 40_0000; // 总数据量
    private int ONE_DB_PAGE_SIZE = 1_0000; // 一次查库数量
    private int PAGE_NUM = 1; // 第几页
    private int TOTAL_PAGES = NumberUtil.ceilDiv(SUM, ONE_DB_PAGE_SIZE); // 总页数
    private int FILE_DATA_NUM = 10_0000; // 单文件数据量
    private int SHEET_DATA_NUM = 10_0000; // 单sheet存储数据量

    @Test
    public void test_multi() throws Exception {
        StopWatch stopWatch = new StopWatch("excel导出-多文件");

        // 定义参数
        int fileNum = NumberUtil.ceilDiv(SUM, FILE_DATA_NUM); // 文件数
        int fileSheetNum = FILE_DATA_NUM / SHEET_DATA_NUM; // 每个文件的sheet数
        for (int fileIndex = 1; fileIndex <= fileNum; fileIndex++) {
            stopWatch.start(StrUtil.format("第{}次", fileIndex));

            log.info("excel-拆分文件 总文件数：{}  处理第{}个文件 ", fileNum, fileIndex);
            BigExcelWriter writer = ExcelUtil.getBigWriter();
            for (int fileSheetIndex = 1; fileSheetIndex <= fileSheetNum; fileSheetIndex++) {
                log.info("excel-拆分文件 总文件数：{}  处理第{}个文件 处理第{}个sheet", fileNum, fileIndex, fileSheetIndex);
                if (fileSheetIndex == 1) {
                    writer.renameSheet("第1个sheet");
                } else {
                    writer.setSheet(StrUtil.format("第{}个sheet", fileSheetIndex));
                }
                writeData(writer);
            }
            FileOutputStream fos = new FileOutputStream(StrUtil.format("D:/test-{}.xlsx", fileIndex));
            writer.flush(fos);
            writer.close();

            stopWatch.stop();
        }
        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Test
    public void test_one() throws Exception {
        StopWatch stopWatch = new StopWatch("excel导出-单文件");
        //        ExcelWriter writer = ExcelUtil.getBigWriter("D:/test.xlsx", "第一个sheet"); // 得是空文件内容才可以写入
//        ExcelWriter writer = ExcelUtil.getWriterWithSheet("第一个sheet");
        BigExcelWriter writer = ExcelUtil.getBigWriter();
        writer.renameSheet("第1个sheet");

        while (PAGE_NUM <= TOTAL_PAGES) {
            log.info("第{}页", PAGE_NUM);
            stopWatch.start(StrUtil.format("第{}次", PAGE_NUM));
            if (PAGE_NUM > 1) {
                writer.setSheet(StrUtil.format("第{}个sheet", PAGE_NUM));
            }
            writeData(writer);
            PAGE_NUM++;
            stopWatch.stop();
        }

        FileOutputStream fos = new FileOutputStream("D:/test.xlsx");
        writer.flush(fos);
        fos.close();
        writer.close();  // 关闭writer，释放内存

        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private void writeData(ExcelWriter writer) {
        for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
            writer.setColumnWidth(columnIndex, 30);
        }
        // 标题
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("time", "时间");

        // 数据 -- 查db逻辑
        List<Map<String, Object>> rowList = Lists.newArrayList();
        for (int i = 0; i < ONE_DB_PAGE_SIZE; i++) {
            ID++;
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", ID);
            map.put("name", RandomUtil.randomString("张三李四王五", 3));
            map.put("time", DateUtil.now());
            rowList.add(map);
        }
        writer.write(rowList);
        //        writer.write(rowList, true); // 一次性写出内容，使用默认样式
        //        writer.setOnlyAlias(true); //只导出设置别名的字段
    }

}
