package com.zhengqing.demo.utils;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.system.entity.TableFileds;
import com.zhengqing.demo.modules.system.entity.Tables;
import com.zhengqing.demo.modules.system.mapper.TableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * <p> 创建word文档 </p>
 *
 * @author : zhengqing
 * @description : 步骤:
 *                  1、建立文档
 *                  2、创建一个书写器
 *                  3、打开文档
 *                  4、向文档中写入数据
 *                  5、关闭文档
 * @date : 2019/11/8 15:45
 */
@Service
public class TableToWordUtil {

    @Autowired
    TableMapper tableMapper;

    /**
     * 生成word文档
     *
     * @param tables：该数据库下所有表信息
     * @param fileName：生成文件地址
     * @param title:文件内容标题
     * @return: void
     */
    public void toWord(List<Tables> tables, String fileName, String title) {
        Document document = new Document(PageSize.A4);
        try {
            // 创建文件夹
            File dir = new File(Constants.FILE_PATH);
            dir.mkdirs();

            // 创建文件
            File file = new File(fileName);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            file.createNewFile();

            // 写入文件信息
            RtfWriter2.getInstance(document, new FileOutputStream(fileName));
            document.open();
            Paragraph ph = new Paragraph();
            Font f = new Font();
            Paragraph p = new Paragraph(title, new Font(Font.NORMAL, 24, Font.BOLDITALIC, new Color(0, 0, 0)));
            p.setAlignment(1);
            document.add(p);
            ph.setFont(f);
            for (int i = 0; i < tables.size(); i++) {
                String table_name = tables.get(i).getName();
                String table_comment = tables.get(i).getComment();
                List<TableFileds> fileds = tableMapper.getTable(tables.get(i).getName());
                String all = "" + (i + 1) + " 表名称:" + table_name + "（" + table_comment + "）";
                Table table = new Table(6);

                document.add(new Paragraph(""));

                table.setBorderWidth(1);
                table.setPadding(0);
                table.setSpacing(0);

                //添加表头的元素，并设置表头背景的颜色
                Color chade = new Color(176, 196, 222);

                Cell cell = new Cell("编号");
                addCell(table, cell, chade);
                cell = new Cell("字段名");
                addCell(table, cell, chade);
                cell = new Cell("类型");
                addCell(table, cell, chade);
                cell = new Cell("是否非空");
                addCell(table, cell, chade);
                cell = new Cell("是否主键");
                addCell(table, cell, chade);
                cell = new Cell("注释");
                addCell(table, cell, chade);

                table.endHeaders();

                // 表格的主体
                for (int k = 0; k < fileds.size(); k++) {
                    addContent(table, cell, (k + 1) + "");
                    addContent(table, cell, fileds.get(k).getField());
                    addContent(table, cell, fileds.get(k).getType());
                    addContent(table, cell, fileds.get(k).getNull().equals("YES") ? "否" : "是");
                    addContent(table, cell, fileds.get(k).getKey() != "" ? "是" : "否");
                    addContent(table, cell, fileds.get(k).getComment());
                }
                Paragraph pheae = new Paragraph(all);
                //写入表说明
                document.add(pheae);
                //生成表格
                document.add(table);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加表头到表格
     *
     * @param table
     * @param cell
     * @param chade
     */
    private void addCell(Table table, Cell cell, Color chade) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(chade);
        table.addCell(cell);
    }

    /**
     * 添加内容到表格
     *
     * @param table
     * @param content
     */
    private void addContent(Table table, Cell cell, String content) {
        cell = new Cell(content);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

}
