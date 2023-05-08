package com.zhengqing.demo.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.Lists;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhengqing.demo.Constants;
import com.zhengqing.demo.bo.UserInfoBO;
import com.zhengqing.demo.enums.ExcelExportFileTypeEnum;
import com.zhengqing.demo.enums.ExcelImportFileTypeEnum;
import com.zhengqing.demo.utils.ExcelUtil;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试接口
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/9/9 10:22
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        List<UserInfoBO> userInfoList = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            userInfoList.add(new UserInfoBO(String.valueOf(i), "张三" + i, String.valueOf(i * 10)));
        }
        List<Map<String, Object>> dataList =
            JSON.parseObject(JSON.toJSONString(userInfoList), new TypeReference<List<Map<String, Object>>>() {});
        ExcelUtil.export(dataList, ExcelExportFileTypeEnum.测试, Constants.FILE_PATH_TEST_EXPORT_EXCEL, response);
    }

    // @PostMapping("/importData")
    @GetMapping("/importData")
    public String importData(@RequestParam(value = "file", required = false) MultipartFile file) {
        List<UserInfoBO> userInfoList = Lists.newArrayList();
        try {
            // 本地File文件转MultipartFile作临时测试前端上传文件导入数据
            File fileLocal = FileUtil.newFile(Constants.FILE_PATH_TEST_EXPORT_EXCEL);
            InputStream inputStream = new FileInputStream(fileLocal);
            MultipartFile multipartFile = new MockMultipartFile(fileLocal.getName(), inputStream);
            ExcelUtil.read(userInfoList, ExcelImportFileTypeEnum.测试, multipartFile, true);
            System.out.println(userInfoList);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

}
