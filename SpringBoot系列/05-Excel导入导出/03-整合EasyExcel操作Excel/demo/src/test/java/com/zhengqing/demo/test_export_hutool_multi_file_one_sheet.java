package com.zhengqing.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
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
 * 多文件，单sheet
 * @date 2024/6/6 1:15
 */
@Slf4j
public class test_export_hutool_multi_file_one_sheet {

    private long ID = 0; // 记录总数
    private int SUM = 30_0003; // 总数据量
    private int ONE_DB_PAGE_SIZE = 1_000; // 一次查库数量
    private int FILE_DATA_NUM = 10_0000; // 单文件数据量

    @Test
    public void test_multi() throws Exception {
        StopWatch stopWatch = new StopWatch("excel导出-多文件");
        stopWatch.start();

        int fileIndex = 1; // 文件数
        int fileWriteDataNum = 0; // 文件写入数
        BigExcelWriter writer = getBigWriter();
        while (true) {
            List<Map<String, Object>> dataList = getDataList(); // 拿到数据 TODO
            if (CollUtil.isEmpty(dataList)) {
                if (fileWriteDataNum > 0) {
                    // 生成文件
                    FileOutputStream fos = new FileOutputStream(StrUtil.format("D:/test-{}.xlsx", fileIndex));
                    writer.flush(fos);
                    writer.close();
                    log.info("excel-拆分文件-{} end 本次写入数据：{}", fileIndex, fileWriteDataNum);
                }
                log.info("【FINISH】excel写入数据 总文件数：{} 总数据量：{}", fileWriteDataNum > 0 ? fileIndex : fileIndex - 1, ID);
                break;
            }

            writer.write(dataList); // 写入数据
            fileWriteDataNum += dataList.size();

            if (fileWriteDataNum >= FILE_DATA_NUM) {
                // 生成文件
                FileOutputStream fos = new FileOutputStream(StrUtil.format("D:/test-{}.xlsx", fileIndex));
                writer.flush(fos);
                writer.close();
                log.info("excel-拆分文件-{} end 本次写入数据：{}", fileIndex, fileWriteDataNum);

                // 继续新文件
                fileIndex++;
                fileWriteDataNum = 0;
                writer = getBigWriter();
            }
        }

        stopWatch.stop();
        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private BigExcelWriter getBigWriter() {
        BigExcelWriter writer = ExcelUtil.getBigWriter();
        for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
            writer.setColumnWidth(columnIndex, 30);
        }
        // 标题
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("time", "时间");
        writer.setOnlyAlias(true); // 设置只导出有别名的字段
        return writer;
    }


    private List<Map<String, Object>> getDataList() {
        List<Map<String, Object>> rowList = Lists.newArrayList();
        if (ID >= SUM) {
            return rowList;
        }
        for (int i = 0; i < ONE_DB_PAGE_SIZE; i++) {
            ID++;
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", ID);
            map.put("name", RandomUtil.randomString("张三李四王五", 3));
            map.put("time", DateUtil.now());
            map.put("updateTime", DateUtil.now());
            rowList.add(map);
            if (ID >= SUM) {
                break;
            }
        }
        return rowList;
    }

}
