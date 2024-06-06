package com.zhengqing.demo;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 写入excel测试 </p>
 *
 * @author zhengqingya
 * @description poi导出
 * @date 2024/6/5 0:08
 */
public class App {

    long sum = 0L; // 记录总数
    int rowIndex = 1; // 每一个工作页的行数

    @Test
    public void test() throws Exception {
        //        创建一个空的工作薄
        Workbook workbook = new SXSSFWorkbook();
        int page = 1;
        int pageSize = 200000;

        Row row = null;
        Cell cell = null;
        Sheet sheet = null;
        int count = 0;

        // 100w 数据以上打开很慢
        while (count < 5) {  //不停地查询
            count++;
            List<User> userList = list();
            if (CollectionUtils.isEmpty(userList)) {  //如果查询不到就不再查询了
                break;
            }
//            if (num % 100000 == 0) {  //每100W个就重新创建新的sheet和标题
            rowIndex = 1;

            // 在工作薄中创建一个工作表
            String sheetName = "第" + count + "个工作表";
            System.err.println(sheetName);
            sheet = workbook.createSheet(sheetName);
            // 设置列宽
            sheet.setColumnWidth(0, 8 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            // 处理标题
            String[] titles = new String[]{"编号", "姓名", "手机号"};
            // 创建标题行
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < titles.length; i++) {
                cell = titleRow.createCell(i);
                cell.setCellValue(titles[i]);
            }

            // 处理内容
            for (User user : userList) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(0);
                cell.setCellValue(user.getId());

                cell = row.createCell(1);
                cell.setCellValue(user.getName());

                cell = row.createCell(2);
                cell.setCellValue(user.getSex());

                rowIndex++;
                sum++;
            }
            page++;// 继续查询下一页
        }

        FileOutputStream outputStream = new FileOutputStream("test.xlsx");
        workbook.write(outputStream);
        System.err.println("总数：" + sum);
    }

    private List<User> list() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 1_0000; i++) {
            sum++;
            userList.add(User.builder().id(sum).name(DateUtil.now()).sex("男").build());
        }
        return userList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private Long id;
        private String name;
        private Integer age;
        private String sex;
    }


}
