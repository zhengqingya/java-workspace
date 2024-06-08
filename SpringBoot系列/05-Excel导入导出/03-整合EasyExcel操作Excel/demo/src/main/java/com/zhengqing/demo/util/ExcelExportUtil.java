package com.zhengqing.demo.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p> Excel导出工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/6/8 23:37
 */
@Slf4j
public class ExcelExportUtil {
    private long dataSum = 0; // 记录总数
    private long oneFileDataNum; // 单文件存储数据量
    private int fileIndex = 0; // 记录写入的文件数
    private BigExcelWriter writer; // excel写入器
    private Map<String, String> headerAliasMap; // 标题
    private boolean isFinishLastFile = true; // 上一个文件是否写入完成
    private String fileNamePrefix; // 文件名前缀 eg: `test-` -> 最终生成文件名`test-01.xlsx`
    private List<File> fileList = Lists.newArrayList(); // 存储的文件信息

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long oneFileDataNum;
        private Map<String, String> headerAliasMap;
        private String fileNamePrefix;

        public Builder oneFileDataNum(Long oneFileDataNum) {
            this.oneFileDataNum = oneFileDataNum;
            return this;
        }

        public Builder headerAliasMap(Map<String, String> headerAliasMap) {
            this.headerAliasMap = headerAliasMap;
            return this;
        }

        public Builder fileNamePrefix(String fileNamePrefix) {
            this.fileNamePrefix = fileNamePrefix;
            return this;
        }

        public ExcelExportUtil build() {
            ExcelExportUtil exportExportUtil = new ExcelExportUtil();
            exportExportUtil.oneFileDataNum = this.oneFileDataNum;
            exportExportUtil.headerAliasMap = this.headerAliasMap;
            exportExportUtil.fileNamePrefix = this.fileNamePrefix;
            return exportExportUtil;
        }
    }


    public void writeData(Iterable<?> data) {
        if (isFinishLastFile) {
            // 新开一个文件进行写入
            writer = ExcelUtil.getBigWriter();
            headerAliasMap.forEach((colName, colDesc) -> writer.addHeaderAlias(colName, colDesc));
            for (int i = 0; i < headerAliasMap.size(); i++) {
                writer.setColumnWidth(i, 20);
            }
            // 设置只导出有别名的字段
            writer.setOnlyAlias(true);
            isFinishLastFile = false;
        }

        // 写入数据
        writer.write(data);
        long oneFileDataIndex = writer.getRowCount() - 1;
        if (oneFileDataIndex >= oneFileDataNum) {
            fileIndex++;
            flushFile();
            isFinishLastFile = true;
            dataSum = oneFileDataIndex;
            log.info("[excel写入] 文件-{} 写入数据量：{}", fileIndex, oneFileDataIndex);
        }
    }

    private void flushFile() {
        if (isFinishLastFile) {
            return;
        }
        File file = FileUtil.newFile(StrUtil.format("{}-{}.xlsx", fileNamePrefix, fileIndex));
        writer.flush(file);
        fileList.add(file);
        writer.close();
    }

    public List<File> getFileList() {
        return this.fileList;
    }

    public void close() {
        flushFile();
        log.info("【FINISH】excel写入数据 总文件数：{} 总数据量：{}", fileIndex, dataSum);
    }

}
