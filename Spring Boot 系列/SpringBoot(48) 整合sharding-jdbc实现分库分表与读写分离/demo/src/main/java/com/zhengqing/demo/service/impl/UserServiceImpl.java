package com.zhengqing.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.mapper.UserMapper;
import com.zhengqing.demo.service.IUserService;
import com.zhengqing.demo.util.IdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    IdGeneratorUtil idGeneratorUtil;

    @Override
    public IPage<User> listPage(User params) {
        IPage<User> result = userMapper.selectDataList(new Page<>(params.getPage(),params.getPageSize()), params);
        List<User> list = result.getRecords();
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addOrUpdateData(User user) {
        if (user.getUserId() == null) {
            user.insert();
        } else {
            user.updateById();
        }
        return user.getUserId();
    }

    @Override
    public String addBatchData(int addSum) {
        LocalDateTime saveBeforeDateTime = LocalDateTime.now();
        // 第1次插入
        int page = 1;
        // 每次插入数据条数
        int pageSize = 5000;
        // 累计插入数量
        int total = 0;
        // 循环插入数据
        for (int index = 1; index <= addSum; ) {
            total = page * pageSize;
            log.info("page:[{}] pageSize:[{}] total:[{}] index:[{}]", page, pageSize, total, index);
            if (total > addSum) {
                int finalNum = addSum - ((page - 1) * pageSize);
                log.info("最后一页新增数：[{}]", finalNum);
                this.insertData(finalNum);
            } else {
                this.insertData(pageSize);
            }
            page += 1;
            index = total + 1;
        }

        LocalDateTime saveAfterDateTime = LocalDateTime.now();
        Duration duration = Duration.between(saveBeforeDateTime, saveAfterDateTime);
        long millis = duration.toMillis();
        String msg = String.format("测试插入%s条数据用时: [%s ms]  [%s s]", addSum, millis, millis / 1000);
        log.info(msg);
        return msg;
    }

    private void insertData(int addSum) {
        List<User> demoList = Lists.newLinkedList();
        for (int i = 1; i <= addSum; i++) {
            User item = new User();
            item.setUserId(idGeneratorUtil.snowflakeId());
            item.setUsername("username - " + i);
            item.setPassword("123456");
            item.setSex((byte)(i % 2));
            item.setRemark("this is username:" + i);
            demoList.add(item);
        }
        this.userMapper.insertBatch(demoList);
    }

}
