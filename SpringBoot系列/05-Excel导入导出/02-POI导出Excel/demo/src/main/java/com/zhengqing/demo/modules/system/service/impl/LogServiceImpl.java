package com.zhengqing.demo.modules.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.demo.modules.common.dto.model.ExcelHeaderInfo;
import com.zhengqing.demo.modules.system.entity.SysLog;
import com.zhengqing.demo.modules.system.enumeration.ExcelFormat;
import com.zhengqing.demo.modules.system.mapper.LogMapper;
import com.zhengqing.demo.modules.system.service.ILogService;
import com.zhengqing.demo.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 系统管理 - 日志表 服务实现类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-18 10:51:57
 */
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
