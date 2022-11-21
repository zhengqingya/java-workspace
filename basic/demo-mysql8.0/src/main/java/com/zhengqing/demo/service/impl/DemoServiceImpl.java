package com.zhengqing.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengqing.demo.entity.Demo;
import com.zhengqing.demo.mapper.DemoMapper;
import com.zhengqing.demo.model.dto.DemoListDTO;
import com.zhengqing.demo.model.dto.DemoSaveDTO;
import com.zhengqing.demo.model.vo.DemoListVO;
import com.zhengqing.demo.service.IDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 测试demo 服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements IDemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public IPage<DemoListVO> listPage(DemoListDTO params) {
        IPage<DemoListVO> result = demoMapper.selectDataList(new Page<>(), params);
        List<DemoListVO> list = result.getRecords();
        this.handleResultData(list);
        return result;
    }

    @Override
    public List<DemoListVO> list(DemoListDTO params) {
        List<DemoListVO> list = demoMapper.selectDataList(params);
        this.handleResultData(list);
        return list;
    }

    /**
     * 处理数据
     *
     * @param list: 数据
     * @return void
     * @author zhengqingya
     * @date 2021/01/13 10:11
     */
    private void handleResultData(List<DemoListVO> list) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addOrUpdateData(DemoSaveDTO params) {
        Integer id = params.getId();
        String username = params.getUsername();
        String password = params.getPassword();

        Demo demo = new Demo();
        demo.setId(id);
        demo.setUsername(username);
        demo.setPassword(password);
        demo.setCreateTime(new Date());

        if (id == null) {
            demo.insert();
        } else {
            demo.updateById();
        }
        return demo.getId();
    }

}
