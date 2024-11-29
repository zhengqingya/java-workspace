package com.zhengqing.demo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.wujiuye.datasource.annotation.EasyMutiDataSource;
import com.github.wujiuye.datasource.tx.TransactionInvokeContext;
import com.zhengqing.demo.entity.Demo;
import com.zhengqing.demo.model.dto.DemoListDTO;
import com.zhengqing.demo.model.dto.DemoSaveDTO;
import com.zhengqing.demo.model.vo.DemoListVO;
import com.zhengqing.demo.service.IDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 测试demo 接口
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequestMapping("/web/api/demo")
@Api(tags = {"测试demo接口"})
@RequiredArgsConstructor
@EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.First)
public class DemoController {

    private final IDemoService demoService;

    @EasyMutiDataSource(EasyMutiDataSource.MultipleDataSource.First)
    @GetMapping("list/page")
    @ApiOperation("列表分页")
    public IPage<DemoListVO> listPage(@ModelAttribute DemoListDTO params) {
        return this.demoService.listPage(params);
    }

    @PostMapping("")
    @ApiOperation("新增")
    @Transactional(rollbackFor = Exception.class)
    public Integer add(@Validated @RequestBody DemoSaveDTO params) {
        checkTransaction();
        Integer i = this.demoService.addOrUpdateData(params);
        if ("123".equals(params.getUsername())) {
            int a = 1 / 0;
        }
        checkTransaction();
        return i;
    }

    private void checkTransaction() {
        // 判断当前调用链路上是否存在事务
        boolean isExistTransaction = TransactionInvokeContext.currentExistTransaction();
        if (isExistTransaction) {
            // 给当前事务绑定一个监听器（PopTransactionListener），当事务提交或者回滚时监听器被调用
            TransactionInvokeContext.addCurrentTransactionMethodPopListener(methodInfo -> {
                log.info("isRollback:{} throwable:{}", methodInfo.isRollback(), methodInfo.getThrowable() == null ? "null" : methodInfo.getThrowable().getMessage());
            });
        }

    }

    @PutMapping("")
    @ApiOperation("更新")
    public Integer update(@Validated @RequestBody DemoSaveDTO params) {
        return this.demoService.addOrUpdateData(params);
    }

    @DeleteMapping("")
    @ApiOperation("删除")
    public void delete(@RequestParam Integer demoId) {
        this.demoService.removeById(demoId);
    }

    @GetMapping("detail")
    @ApiOperation("详情")
    public Demo detail(@RequestParam Integer demoId) {
        return this.demoService.getById(demoId);
    }
}
