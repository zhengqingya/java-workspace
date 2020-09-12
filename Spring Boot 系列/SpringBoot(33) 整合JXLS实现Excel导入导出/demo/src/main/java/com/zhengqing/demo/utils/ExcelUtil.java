package com.zhengqing.demo.utils;

import cn.hutool.core.io.FileUtil;
import com.zhengqing.demo.Constants;
import com.zhengqing.demo.enums.ExcelExportFileTypeEnum;
import com.zhengqing.demo.enums.ExcelImportFileTypeEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.common.Context;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 导入导出Excel报表工具类
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/9/7 14:26
 */
@Slf4j
public class ExcelUtil {

    /**
     * 读取上传文件数据
     *
     * @param dataList:
     *            数据
     * @param excelImportFileTypeEnum:
     *            导入报表模板类型
     * @param file:
     *            上传文件数据
     * @param isThrowException:
     *            遇到错误是否抛出异常信息 true:抛出 false：不抛，继续处理数据
     * @return: 装满数据的dataList
     * @author : zhengqing
     * @date : 2020/9/7 13:59
     */
    @SneakyThrows(Exception.class)
    public static <E, T> List<T> read(List<T> dataList, ExcelImportFileTypeEnum excelImportFileTypeEnum,
        MultipartFile file, boolean isThrowException) {
        String fileName = file.getName();
        InputStream inputXLS = null;
        InputStream inputXML = null;
        try {
            Resource resource =
                new ClassPathResource(Constants.DEFAULT_REPORT_IMPORT_FOLDER + excelImportFileTypeEnum.getMappingXml());
            // 上传文件流
            inputXLS = file.getInputStream();
            // xml配置文件流
            inputXML = resource.getInputStream();
            // 执行解析
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
            Map<String, Object> beans = new HashMap<>(1);
            beans.put("dataList", dataList);
            XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
            if (readStatus.isStatusOK()) {
                log.debug("读取excel文件成功: 【{}】", fileName);
            }
        } catch (Exception e) {
            // ① 记录错误位置
            String errorCell = e.getMessage().split(" ")[3];
            // ② 记录错误原因
            String errorMsg = e.getCause().toString();
            String[] causeMsgArray = errorMsg.split(":");
            errorMsg = errorMsg.substring(causeMsgArray[0].length() + 2).split(":")[0];
            switch (errorMsg) {
                case "For input string":
                    errorMsg = "时间格式不正确";
                    break;
                case "Error converting from 'String' to 'Integer' For input string":
                    errorMsg = "请填写数字类型";
                    break;
                default:
                    break;
            }
            errorMsg = "读取" + fileName + "文件异常: " + errorCell + errorMsg;
            if (isThrowException) {
                throw new Exception(errorMsg);
            } else {
                log.error(errorMsg);
            }
        } finally {
            try {
                if (inputXLS != null) {
                    inputXLS.close();
                }
                if (inputXML != null) {
                    inputXML.close();
                }
            } catch (IOException e) {
                log.error("parse excel error : 【{}】", e.getMessage());
            }
        }
        return dataList;
    }

    /**
     * 导出EXCEL到指定路径
     *
     * @param dataList:
     *            数据
     * @param excelExportFileTypeEnum:
     *            导出报表模板类型
     * @param exportPath:
     *            导出路径
     * @return: 文件下载地址信息
     * @author : zhengqing
     * @date : 2020/9/7 13:59
     */
    @SneakyThrows(Exception.class)
    public static String export(List<Map<String, Object>> dataList, ExcelExportFileTypeEnum excelExportFileTypeEnum,
        String exportPath) {
        // 处理导出
        File exportFile = handleExport(dataList, excelExportFileTypeEnum, exportPath);
        String fileName = excelExportFileTypeEnum.getSheetName() + ".xls";
        // TODO 这里可以对`exportFile`做文件上传处理，然后返回一个文件下载地址 或其它业务处理...
        return exportFile.getAbsolutePath();
    }

    /**
     * 导出EXCEL给前端直接下载
     *
     * @param dataList:
     *            数据
     * @param excelExportFileTypeEnum:
     *            导出报表模板类型
     * @param exportPath:
     *            导出路径
     * @param response:
     * @return: void
     * @author : zhengqing
     * @date : 2020/9/8 14:59
     */
    @SneakyThrows(Exception.class)
    public static void export(List<Map<String, Object>> dataList, ExcelExportFileTypeEnum excelExportFileTypeEnum,
        String exportPath, HttpServletResponse response) {
        // 处理导出
        handleExport(dataList, excelExportFileTypeEnum, exportPath);

        // ======================= ↓↓↓↓↓↓ 响应给前端 ↓↓↓↓↓↓ =======================
        // 文件名 - 解决中文乱码问题
        String filename = URLEncoder.encode(excelExportFileTypeEnum.getTemplateFile().substring(1), "UTF-8");
        // 设置响应编码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = new FileInputStream(exportPath);
        byte[] buffer = new byte[1024];
        int i = -1;
        while ((i = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, i);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    /**
     * 处理导出数据逻辑
     *
     * @param dataList:
     *            数据
     * @param excelExportFileTypeEnum:
     *            导出报表模板类型
     * @param exportPath:
     *            导出路径
     * @return: 导出数据文件
     * @author : zhengqing
     * @date : 2020/9/8 15:49
     */
    @SneakyThrows(Exception.class)
    private static File handleExport(List<Map<String, Object>> dataList,
        ExcelExportFileTypeEnum excelExportFileTypeEnum, String exportPath) {
        Resource resource =
            new ClassPathResource(Constants.DEFAULT_REPORT_EXPORT_FOLDER + excelExportFileTypeEnum.getTemplateFile());
        InputStream templateInputStream = resource.getInputStream();

        log.debug("导出文件地址为:{}", exportPath);

        // 创建文件
        File exportFile = FileUtil.touch(exportPath);

        // 列表数据将存储到指定的excel文件路径
        OutputStream out = new FileOutputStream(exportPath);
        // 这里的context是jxls框架上的context内容
        Context context = PoiTransformer.createInitialContext();
        // 将列表参数放入context中
        context.putVar("dataList", dataList);
        Workbook workbook = WorkbookFactory.create(templateInputStream);
        // Changing name of the first sheet
        workbook.setSheetName(0, excelExportFileTypeEnum.getSheetName());
        PoiTransformer transformer = PoiTransformer.createTransformer(workbook);
        transformer.setOutputStream(out);
        // 将列表数据按照模板文件中的格式生成
        JxlsHelper.getInstance().processTemplate(context, transformer);
        templateInputStream.close();
        out.close();
        return exportFile;
    }

}
