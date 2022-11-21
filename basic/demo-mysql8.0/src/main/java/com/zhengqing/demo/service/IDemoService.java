package com.zhengqing.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhengqing.demo.entity.Demo;
import com.zhengqing.demo.model.dto.DemoListDTO;
import com.zhengqing.demo.model.dto.DemoSaveDTO;
import com.zhengqing.demo.model.vo.DemoListVO;

import java.util.List;

/**
 * <p>
 * 测试demo 服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface IDemoService extends IService<Demo> {

    /**
     * 列表分页
     *
     * @param params: 查询参数
     * @return 查询结果
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    IPage<DemoListVO> listPage(DemoListDTO params);

    /**
     * 列表
     *
     * @param params: 查询参数
     * @return 查询结果
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    List<DemoListVO> list(DemoListDTO params);

    /**
     * 新增或更新
     *
     * @param params: 保存参数
     * @return 主键id
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    Integer addOrUpdateData(DemoSaveDTO params);

}
