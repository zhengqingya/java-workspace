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
 * <p> EasyExcel 根据数据相同导出动态合并单元格 </p>
 *
 * @author zhengqingya
 * @description 把相同数据合并没有进行判断
 * @date 2022/1/25 11:45
 */
@AllArgsConstructor
public class ExcelFillCellMergeStrategy implements CellWriteHandler {

    /**
     * 从第?行后开始合并
     */
    private final int mergeRowIndex;
    /**
     * 需要合并的列
     */
    private final int[] mergeColumnIndex;

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        Cell cell = context.getCell();
        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();

        if (curRowIndex > this.mergeRowIndex) {
            for (int i = 0; i < this.mergeColumnIndex.length; i++) {
                if (curColIndex == this.mergeColumnIndex[i]) {
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
        //获取当前行的第一列的数据和上一行的第一列数据，通过第一行数据是否相同进行合并
//        Cell preCell_now = cell.getSheet().getRow(curRowIndex ).getCell(curColIndex);
//        Object curData = preCell_now.getCellTypeEnum() == CellType.STRING ? preCell_now.getStringCellValue() : preCell_now.getNumericCellValue();
//        Cell preCell = cell.getSheet().getRow(curRowIndex - 1).getCell(curColIndex - 1);
        //      Object preData = preCell.getCellTypeEnum() == CellType.STRING ? preCell.getStringCellValue() : preCell.getNumericCellValue();
        //获取当前行的当前列的数据和上一行的当前列列数据，通过上一行数据是否相同进行合并
        Object curData = cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();
        Cell preCell = cell.getSheet().getRow(curRowIndex - 1).getCell(curColIndex);
        Object preData = preCell.getCellTypeEnum() == CellType.STRING ? preCell.getStringCellValue() : preCell.getNumericCellValue();

        // 比较当前行的第一列的单元格与上一行是否相同，相同合并当前单元格与上一行
        //
        if (curData.equals(preData)) {
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

