package com.zhengqing.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 *  <p> </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/11/4 18:33
 */
@Mapper
public interface CronMapper {

    @Select("select cron from cron limit 1")
    String getCron();

}
