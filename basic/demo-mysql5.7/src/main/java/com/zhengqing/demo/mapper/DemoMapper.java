package com.zhengqing.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhengqing.demo.entity.Demo;
import com.zhengqing.demo.model.dto.DemoListDTO;
import com.zhengqing.demo.model.vo.DemoListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 测试demo Mapper 接口
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
public interface DemoMapper extends BaseMapper<Demo> {

    /**
     * 列表分页
     *
     * @param page:   分页数据
     * @param filter: 查询过滤参数
     * @return 查询结果
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    IPage<DemoListVO> selectDataList(IPage page, @Param("filter") DemoListDTO filter);

    /**
     * 列表
     *
     * @param filter: 查询过滤参数
     * @return 查询结果
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    List<DemoListVO> selectDataList(@Param("filter") DemoListDTO filter);

}
