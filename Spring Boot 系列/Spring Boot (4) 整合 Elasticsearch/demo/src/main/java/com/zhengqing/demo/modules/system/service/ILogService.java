package com.zhengqing.demo.modules.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.demo.modules.system.entity.SysLog;

/**
 * <p>  系统管理 - 日志表 服务类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-18 10:51:57
 */
public interface ILogService extends IService<SysLog> {

    SysLog saveOrUpdate(SysLog syslog);

}
