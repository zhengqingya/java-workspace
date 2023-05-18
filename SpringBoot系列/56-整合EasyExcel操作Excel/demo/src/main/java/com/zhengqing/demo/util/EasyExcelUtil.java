package com.zhengqing.demo.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.zhengqing.demo.easyexcel.handler.MyCellWriteHandler;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p> EasyExcel工具类 </p>
 *
 * @author zhengqingya
 * @description https://www.yuque.com/easyexcel/doc/easyexcel
 * @date 2022/1/24 17:17
 */
public class EasyExcelUtil {

    /**
     * 动态合并单元格
     *
     * @param response      响应
     * @param fileName      文件名 -- 无后缀
     * @param fileName      sheet名
     * @param clz           导出数据模型实体类
     * @param dataList      导出数据
     * @param mergeColIndex 需要合并的列
     * @param mergeRowIndex 从第？行后开始合并
     * @return void
     * @author zhengqingya
     * @date 2022/1/25 14:11
     */
    @SneakyThrows(Exception.class)
    public static void dynamicMergeData(HttpServletResponse response, String fileName, String sheetName,
                                        Class<?> clz, List<?> dataList, int[] mergeColIndex, int mergeRowIndex) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 此header解决前端下载不了文件问题
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        // 调用合并单元格工具类，此工具类是没有根据合并，数据相同就合并了
//        ExcelFillCellMergeStrategy excelFillCellMergeStrategy = new ExcelFillCellMergeStrategy(mergeRowIndex, mergeColIndex);

        // 调用合并单元格工具类，此工具类是根据工程名称相同则合并后面数据
        MyCellWriteHandler excelFillCellMergeStrategy = new MyCellWriteHandler(mergeRowIndex, mergeColIndex);
        // 这里需要设置不关闭流
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream(), clz)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .registerWriteHandler(excelFillCellMergeStrategy)
                // 导出文件名
                .autoCloseStream(Boolean.TRUE).sheet(sheetName)
                .doWrite(dataList);
    }

}
