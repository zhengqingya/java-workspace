package com.zhengqing.demo.modules.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.demo.modules.system.entity.SysLog;
import com.zhengqing.demo.modules.system.mapper.LogMapper;
import com.zhengqing.demo.modules.system.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
