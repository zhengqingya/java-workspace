package com.zhengqing.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
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
import java.util.stream.Collectors;

/**
 * <p> 测试导出 </p>
 *
 * @author zhengqingya
 * @description https://hutool.cn/docs/#/poi/Excel%E5%A4%A7%E6%95%B0%E6%8D%AE%E7%94%9F%E6%88%90-BigExcelWriter
 * @date 2024/6/6 1:15
 */
@Slf4j
public class TestExportHutool {


    @Test
    public void test() throws Exception {
        List<?> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
        List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
        List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
        List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
        List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);

        List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

        BigExcelWriter writer = ExcelUtil.getBigWriter("D:/test.xlsx");
        // 一次性写出内容，使用默认样式
        writer.write(rows);
        // 关闭writer，释放内存
        writer.close();
    }

    @Test
    public void test_02() throws Exception {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> rows = Lists.newArrayList(1, 2, 3, 4, 5, 6).stream().map(item -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", IdUtil.fastSimpleUUID());
            map.put("time", DateUtil.now());
            return map;
        }).collect(Collectors.toList());

        ExcelWriter writer = ExcelUtil.getBigWriter();
        for (int i = 0; i < 2; i++) {
            writer.setColumnWidth(i, 35);
        }
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("time", "时间");

        writer.write(rows, true);
        writer.setOnlyAlias(true);
        FileOutputStream outputStream = new FileOutputStream("D:/test.xlsx");
        writer.flush(outputStream);
        writer.close();

        long endTime = System.currentTimeMillis();
        log.info("Hutool 写入 " + rows.size() + " 条记录耗时 " + (endTime - startTime) / 1000 + "秒");
    }

}
