package com.zhengqing.demo;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelWriteTest {

   @Test
   public void writeWithHead() throws IOException {

      // 创建文件
      File file = new File("测试.xlsx");
      if (file.exists() && file.isFile()) {
         file.delete();
      }
      file.createNewFile();

      try (OutputStream out = new FileOutputStream("测试.xlsx");) {
         ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
         Sheet sheet1 = new Sheet(1, 0, ExcelPropertyIndexModel.class);
         sheet1.setSheetName("sheet1");
         List<ExcelPropertyIndexModel> data = new ArrayList<>();
         for (int i = 0; i < 100; i++) {
            ExcelPropertyIndexModel item = new ExcelPropertyIndexModel();
            item.name = "name" + i;
            item.age = "age" + i;
            item.email = "email" + i;
            item.address = "address" + i;
            item.sax = "sax" + i;
            item.heigh = "heigh" + i;
            item.last = "last" + i;
            data.add(item);
         }
         writer.write(data, sheet1);
         writer.finish();
      }
   }

   @Data
   public static class ExcelPropertyIndexModel extends BaseRowModel {

      @ExcelProperty(value = "姓名", index = 0)
      private String name;

      @ExcelProperty(value = "年龄", index = 1)
      private String age;

      @ExcelProperty(value = "邮箱", index = 2)
      private String email;

      @ExcelProperty(value = "地址", index = 3)
      private String address;

      @ExcelProperty(value = "性别", index = 4)
      private String sax;

      @ExcelProperty(value = "高度", index = 5)
      private String heigh;

      @ExcelProperty(value = "备注", index = 6)
      private String last;
   }
}
