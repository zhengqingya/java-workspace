package com.zhengqing.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.zhengqing.demo.util.ExcelExportUtil;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 测试导出 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/6/6 1:04
 */
public class test_export_multi_file {

    long id = 0;
    long sum = 100_0000;
    long oneDbPageSize = 10000;

    @Test
    public void test01() throws Exception {
        // 1、初始化参数
        ExcelExportUtil exportUtil = ExcelExportUtil.builder()
                .oneFileDataNum(10_0000L)
                .headerAliasMap(new LinkedHashMap<String, String>() {{
                    put("id", "序号");
                    put("name", "名称");
                    put("time", "时间");
                }})
                .fileNamePrefix("D:/tmp/file/test")
                .build();

        // 2、从db拉取数据进行写入 -- 循环
        while (true) {
            List<Map<String, Object>> dataList = getDataList();
            if (CollUtil.isEmpty(dataList)) {
                break;
            }
            exportUtil.writeData(dataList);
        }

        // 3、拿到写入的文件资源 File
        List<File> fileList = exportUtil.getFileList();

        // 4、关闭资源
        exportUtil.close();
    }

    private List<Map<String, Object>> getDataList() {
        List<Map<String, Object>> rowList = Lists.newArrayList();
        if (id >= sum) {
            return rowList;
        }
        for (int i = 0; i < oneDbPageSize; i++) {
            id++;
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("name", RandomUtil.randomString("张三李四王五", 3));
            map.put("time", DateUtil.now());
            map.put("updateTime", DateUtil.now());
            rowList.add(map);
            if (id >= sum) {
                break;
            }
        }
        return rowList;
    }

}
