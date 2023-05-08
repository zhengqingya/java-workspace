# POI 导出 Excel

### 1、 `pom.xml` 中 引入所需依赖
```xml
<!-- ======================== 导出excel所需依赖 ============================ -->
<!-- poi -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>4.0.1</version>
</dependency>

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>4.0.1</version>
</dependency>

<!-- beanutils -->
<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.9.3</version>
</dependency>
```

### 2、Excel报表导出工具类
```java
package com.zhengqing.demo.utils;

import com.zhengqing.demo.modules.common.dto.model.ExcelHeaderInfo;
import com.zhengqing.demo.modules.system.enumeration.ExcelFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  <p> Excel报表导出工具类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/11/27 11:02
 */
public class ExcelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    public static final int ROW_ACCESS_WINDOW_SIZE = 100;
    public static final int SHEET_MAX_ROW = 100000;

    private List list;
    private List<ExcelHeaderInfo> excelHeaderInfos;
    private Map<String, ExcelFormat> formatInfo;

    public ExcelUtils(List list, List<ExcelHeaderInfo> excelHeaderInfos) {
        this.list = list;
        this.excelHeaderInfos = excelHeaderInfos;
    }

    public ExcelUtils(List list, List<ExcelHeaderInfo> excelHeaderInfos, Map<String, ExcelFormat> formatInfo) {
        this.list = list;
        this.excelHeaderInfos = excelHeaderInfos;
        this.formatInfo = formatInfo;
    }

    public Workbook getWorkbook() {
        Workbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        String[][] datas = transformData();
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        CellStyle style = setCellStyle(workbook);
        int pageRowNum = 0;
        Sheet sheet = null;

        long startTime = System.currentTimeMillis();
        LOGGER.info("开始处理excel文件。。。");

        for (int i = 0; i < datas.length; i++) {
            // 如果是每个sheet的首行
            if (i % SHEET_MAX_ROW == 0) {
                // 创建新的sheet
                sheet = createSheet(workbook, i);
                pageRowNum = 0; // 行号重置为0
                for (int j = 0; j < getHeaderRowNum(excelHeaderInfos); j++) {
                    sheet.createRow(pageRowNum++);
                }
                createHeader(sheet, style);
            }
            // 创建内容
            Row row = sheet.createRow(pageRowNum++);
            createContent(row, style, datas, i, fields);
        }
        LOGGER.info("处理文本耗时" + (System.currentTimeMillis() - startTime) + "ms");
        return workbook;
    }

    // 创建表头
    private void createHeader(Sheet sheet, CellStyle style) {
        for (ExcelHeaderInfo excelHeaderInfo : excelHeaderInfos) {
            Integer lastRow = excelHeaderInfo.getLastRow();
            Integer firstRow = excelHeaderInfo.getFirstRow();
            Integer lastCol = excelHeaderInfo.getLastCol();
            Integer firstCol = excelHeaderInfo.getFirstCol();

            // 行距或者列距大于0才进行单元格融合
            if ((lastRow - firstRow) != 0 || (lastCol - firstCol) != 0) {
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
            }
            // 获取当前表头的首行位置
            Row row = sheet.getRow(firstRow);
            // 在表头的首行与首列位置创建一个新的单元格
            Cell cell = row.createCell(firstCol);
            // 赋值单元格
            cell.setCellValue(excelHeaderInfo.getTitle());
            cell.setCellStyle(style);
            sheet.setColumnWidth(firstCol, sheet.getColumnWidth(firstCol) * 17 / 12);
        }
    }

    // 创建正文
    private void createContent(Row row, CellStyle style, String[][] content, int i, Field[] fields) {
        List<String> columnNames = getBeanProperty(fields);
        for (int j = 0; j < columnNames.size(); j++) {
            // 如果格式化Map为空，默认为字符串格式
            if (formatInfo == null) {
                row.createCell(j).setCellValue(content[i][j]);
                continue;
            }
            if (formatInfo.containsKey(columnNames.get(j))) {
                switch (formatInfo.get(columnNames.get(j)).getValue()) {
                    case "DOUBLE":
                        row.createCell(j).setCellValue(Double.parseDouble(content[i][j]));
                        break;
                    case "INTEGER":
                        row.createCell(j).setCellValue(Integer.parseInt(content[i][j]));
                        break;
                    case "PERCENT":
                        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
                        Cell cell = row.createCell(j);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(content[i][j]));
                        break;
                    case "DATE":
                        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
                        Cell cell1 = row.createCell(j);
                        cell1.setCellStyle(style);
                        row.createCell(j).setCellValue(this.parseDate(content[i][j]));
                }
            } else {
                row.createCell(j).setCellValue(content[i][j]);
            }
        }
    }

    // 将原始数据转成二维数组
    private String[][] transformData() {
        int dataSize = this.list.size();
        String[][] datas = new String[dataSize][];
        // 获取报表的列数
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        // 获取实体类的字段名称数组
        List<String> columnNames = this.getBeanProperty(fields);
        for (int i = 0; i < dataSize; i++) {
            datas[i] = new String[fields.length];
            for (int j = 0; j < fields.length; j++) {
                try {
                    // 赋值
                    datas[i][j] = BeanUtils.getProperty(list.get(i), columnNames.get(j));
                } catch (Exception e) {
                    LOGGER.error("获取对象属性值失败");
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }

    // 获取实体类的字段名称数组
    private List<String> getBeanProperty(Field[] fields) {
        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            String[] strings = field.toString().split("\\.");
            String columnName = strings[strings.length - 1];
            columnNames.add(columnName);
        }
        return columnNames;
    }

    // 新建表格
    private static Sheet createSheet(Workbook workbook, int i) {
        Integer sheetNum = i / SHEET_MAX_ROW;
        workbook.createSheet("sheet" + sheetNum);
        //动态指定当前的工作表
        return workbook.getSheetAt(sheetNum);
    }

    // 获取标题总行数
    private static Integer getHeaderRowNum(List<ExcelHeaderInfo> headerInfos) {
        Integer maxRowNum = 0;
        for (ExcelHeaderInfo excelHeaderInfo : headerInfos) {
            Integer tmpRowNum = excelHeaderInfo.getLastRow();
            if (tmpRowNum > maxRowNum) maxRowNum = tmpRowNum;
        }
        return maxRowNum + 1;
    }

    // 设置总体表格样式
    private static CellStyle setCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    // 字符串转日期
    private Date parseDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (Exception e) {
            LOGGER.error("字符串转日期错误");
            e.printStackTrace();
        }
        return date;
    }

    // 发送响应结果
    public void sendHttpResponse(HttpServletResponse response, String fileName, Workbook workbook) {
        try {
            fileName += System.currentTimeMillis() + ".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 3、Excel表头信息
```java
package com.zhengqing.demo.modules.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p> Excel表头信息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/27 18:14
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ExcelHeaderInfo {

    /**
     * 标题的首行坐标
     */
    private int firstRow;
    /**
     * 标题的末行坐标
     */
    private int lastRow;
    /**
     * 标题的首列坐标
     */
    private int firstCol;
    /**
     * 标题的末列坐标
     */
    private int lastCol;
    /**
     * 标题
     */
    private String title;

}
```

### 4、使用举例
```java
@Service
@Transactional
public class LogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements ILogService {

    @Autowired
    LogMapper logMapper;

    @Override
    public void export(HttpServletResponse response, String fileName) {
        // 待导出数据
        List<SysLog> logList = logMapper.selectList(null);
        ExcelUtils excelUtils = new ExcelUtils(logList, getHeaderInfo(), getFormatInfo());
        excelUtils.sendHttpResponse(response, fileName, excelUtils.getWorkbook());
    }

    /**
     * 获取表头信息
     *
     * @param :
     * @return: java.util.List<com.zhengqing.demo.modules.common.dto.model.ExcelHeaderInfo>
     */
    private List<ExcelHeaderInfo> getHeaderInfo() {
        return Arrays.asList(
                new ExcelHeaderInfo(1, 1, 0, 0, "id"),

                new ExcelHeaderInfo(0, 0, 1, 3, "接口信息"),
                new ExcelHeaderInfo(1, 1, 1, 1, "接口名称"),
                new ExcelHeaderInfo(1, 1, 2, 2, "接口地址"),
                new ExcelHeaderInfo(1, 1, 3, 3, "接口执行时间"),

                new ExcelHeaderInfo(0, 0, 4, 7, "访问信息"),
                new ExcelHeaderInfo(1, 1, 4, 4, "访问人IP"),
                new ExcelHeaderInfo(1, 1, 5, 5, "访问人ID"),
                new ExcelHeaderInfo(1, 1, 6, 6, "状态"),
                new ExcelHeaderInfo(1, 1, 7, 7, "访问人名字"),

                new ExcelHeaderInfo(1, 1, 8, 8, "创建时间"),
                new ExcelHeaderInfo(1, 1, 9, 9, "修改时间")
        );
    }

    /**
     * 获取格式化信息
     *
     * @param :
     * @return: java.util.Map<java.lang.String,com.zhengqing.demo.modules.system.enumeration.ExcelFormat>
     */
    private Map<String, ExcelFormat> getFormatInfo() {
        Map<String, ExcelFormat> format = new HashMap<>();
        format.put("id", ExcelFormat.FORMAT_INTEGER);
        format.put("userId", ExcelFormat.FORMAT_INTEGER);
        format.put("status", ExcelFormat.FORMAT_INTEGER);
        return format;
    }

}
```

### 案例demo源码
https://gitee.com/zhengqingya/java-workspace
