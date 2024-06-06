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

    private long ID = 0;
    private int SUM = 100_0000;
    private int ONE_NUM = 1_0000;
    long COUNT = NumberUtil.ceilDiv(SUM, ONE_NUM);

    @Test
    public void test_01() throws Exception {
        StopWatch stopWatch = new StopWatch("excel导出");
        stopWatch.start("第1次");

        //        ExcelWriter writer = ExcelUtil.getBigWriter("D:/test.xlsx", "第一个sheet"); // 得是空文件内容才可以写入
//        ExcelWriter writer = ExcelUtil.getWriterWithSheet("第一个sheet");
        BigExcelWriter writer = ExcelUtil.getBigWriter();

        for (int i = 0; i < COUNT; i++) {
            for (int columnIndex = 0; columnIndex < 2; i++) {
                writer.setColumnWidth(columnIndex, 35);
            }
            writer.renameSheet("第1个sheet");

            writer.addHeaderAlias("id", "ID");
            writer.addHeaderAlias("name", "名称");
            writer.addHeaderAlias("time", "时间");

            if (i > 0) {
                writer.setSheet(StrUtil.format("第{}个sheet", i + 2));
            }
            writeData(writer);
        }

        FileOutputStream fos = new FileOutputStream("D:/test.xlsx");
        writer.flush(fos);
        fos.close();
        writer.close();  // 关闭writer，释放内存

        stopWatch.stop();
        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private void writeData(ExcelWriter writer) {
        List<Map<String, Object>> rowList = Lists.newArrayList();
        for (int i = 0; i < ONE_NUM; i++) {
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
