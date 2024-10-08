package com.zhengqing.demo;

import cn.hutool.core.collection.CollUtil;
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
public class TestExportHutoolMulti {

    private long ID = 0; // 记录总数
    private int SUM = 5_0000; // 总数据量
    private int ONE_DB_PAGE_SIZE = 1_0000; // 一次查库数量
    private int PAGE_NUM = 1; // 第几页
    private int TOTAL_PAGES = NumberUtil.ceilDiv(SUM, ONE_DB_PAGE_SIZE); // 总页数
    private int FILE_DATA_NUM = 10_0000; // 单文件数据量
    private int SHEET_DATA_NUM = 1_0000; // 单sheet存储数据量
    private int SHEET_NUM = 3; // sheet个数

    @Test
    public void test_multi() throws Exception {
        StopWatch stopWatch = new StopWatch("excel导出-多文件");

        int fileIndex = 1; // 文件数
        boolean isGo = true; // 是否继续下一个文件
        while (isGo) {
            stopWatch.start(StrUtil.format("第{}个文件", fileIndex));
            log.info("excel-拆分文件-{} start", fileIndex);
            BigExcelWriter writer = ExcelUtil.getBigWriter();

            boolean isCreateFile = true; // 是否需要写出文件
            for (int sheetIndex = 1; sheetIndex <= SHEET_NUM; sheetIndex++) {
                List<Map<String, Object>> dataList = getDataList(); // 拿到数据 TODO
                if (CollUtil.isEmpty(dataList)) {
                    isGo = false;
                    isCreateFile = sheetIndex != 1;
                    if (!isCreateFile) {
                        log.info("excel-拆分文件-{} sheet-{} 无数据写入", fileIndex, sheetIndex);
                    }
                    break;
                }
                if (sheetIndex == 1) {
                    writer.renameSheet("第1个sheet");
                } else {
                    writer.setSheet(StrUtil.format("第{}个sheet", sheetIndex));
                }
                log.info("excel-拆分文件-{} sheet-{} 写入数据量：{}", fileIndex, sheetIndex, dataList.size());
                writeData(writer, dataList); // 写入数据
            }

            log.info("excel-拆分文件-{} end", fileIndex);
            if (isCreateFile) {
                FileOutputStream fos = new FileOutputStream(StrUtil.format("D:/test-{}.xlsx", fileIndex));
                writer.flush(fos);
            }

            if (!isGo) {
                log.info("【FINISH】excel写入数据 总文件数：{} 总数据量：{}", isCreateFile ? fileIndex : fileIndex - 1, ID);
            }
            writer.close();
            fileIndex++;
            stopWatch.stop();
        }

        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private void writeData(ExcelWriter writer, List<Map<String, Object>> dataList) {
        for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
            writer.setColumnWidth(columnIndex, 30);
        }

        // 标题
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("time", "时间");

        writer.setOnlyAlias(true); // 设置只导出有别名的字段
        writer.write(dataList);
    }

    private List<Map<String, Object>> getDataList() {
        // 模拟数据 -- 查db逻辑
        if (ID >= SUM) {
            return Lists.newArrayList();
        }
        List<Map<String, Object>> rowList = Lists.newArrayList();
        for (int i = 0; i < ONE_DB_PAGE_SIZE; i++) {
            ID++;
            if (ID > SUM) {
                break;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", ID);
            map.put("name", RandomUtil.randomString("张三李四王五", 3));
            map.put("time", DateUtil.now());
            map.put("updateTime", DateUtil.now());
            rowList.add(map);
        }
        return rowList;
    }


}
