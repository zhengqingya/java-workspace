package com.zhengqing.demo.easyexcel.handler;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;


/**
 * <p> EasyExcel 动态合并单元格 </p>
 *
 * @author zhengqingya
 * @description 根据指定数据合并
 * @date 2022/1/25 11:45
 */
@AllArgsConstructor
public class MyCellWriteHandler implements CellWriteHandler {

    /**
     * 从第?行后开始合并
     */
    private final int mergeRowIndex;
    /**
     * 需要合并的列
     */
    private final int[] mergeColIndex;

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        Cell cell = context.getCell();
        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();

        if (curRowIndex > this.mergeRowIndex) {
            for (int i = 0; i < this.mergeColIndex.length; i++) {
                if (curColIndex == this.mergeColIndex[i]) {
                    this.mergeWithPrevRow(writeSheetHolder, cell, curRowIndex, curColIndex);
                    break;
                }
            }
        }
    }


    /**
     * 当前单元格向上合并
     *
     * @param writeSheetHolder excel
     * @param cell             当前单元格
     * @param curRowIndex      当前行
     * @param curColIndex      当前列
     */
    private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {
        Object curData = cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();
        Cell preCell = cell.getSheet().getRow(curRowIndex - 1).getCell(curColIndex);
        Object preData = preCell.getCellTypeEnum() == CellType.STRING ? preCell.getStringCellValue() : preCell.getNumericCellValue();
        // 将当前单元格数据与上一个单元格数据比较
        Boolean dataBool = preData.equals(curData);
        // 获取每一行第二列数据和上一行第一列数据进行比较，如果相等合并，getCell里面的值，是所需合并名称所在列的下标
        Boolean bool = cell.getRow().getCell(1).getStringCellValue().equals(cell.getSheet().getRow(curRowIndex - 1).getCell(1).getStringCellValue());
        if (dataBool && bool) {
            Sheet sheet = writeSheetHolder.getSheet();
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            boolean isMerged = false;
            for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
                    sheet.removeMergedRegion(i);
                    cellRangeAddr.setLastRow(curRowIndex);
                    sheet.addMergedRegion(cellRangeAddr);
                    isMerged = true;
                }
            }
            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
                sheet.addMergedRegion(cellRangeAddress);
            }
        }
    }

}


