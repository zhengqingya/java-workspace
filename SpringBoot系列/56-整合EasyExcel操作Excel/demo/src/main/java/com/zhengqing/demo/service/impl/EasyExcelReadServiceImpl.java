package com.zhengqing.demo.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.zhengqing.demo.easyexcel.listener.DemoDataListener;
import com.zhengqing.demo.mapper.DemoMapper;
import com.zhengqing.demo.model.dto.DemoData;
import com.zhengqing.demo.service.IEasyExcelReadService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * EasyExcel 业务服务实现类
 * </p>
 *
 * @author zhengqingya
 * @description https://www.yuque.com/easyexcel/doc/easyexcel
 * @date 2021/01/13 10:11
 */
@Slf4j
@Service
public class EasyExcelReadServiceImpl implements IEasyExcelReadService {

    @Resource
    private DemoMapper demoMapper;

    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>
     * 3. 直接读即可
     */
    @Override
    @SneakyThrows(Exception.class)
    public void simpleRead(MultipartFile file) {
        // FIRST METHOD
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(file.getInputStream(), DemoData.class, new DemoDataListener()).sheet().doRead();


        // TWO METHOD
        EasyExcel.read(file.getInputStream(), DemoData.class, new ReadListener<DemoData>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             *临时存储
             */
            private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(DemoData data, AnalysisContext context) {
                this.cachedDataList.add(data);
                if (this.cachedDataList.size() >= BATCH_COUNT) {
                    this.saveData();
                    // 存储完成清理 list
                    this.cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                this.saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", this.cachedDataList.size());
                log.info("存储数据库成功！");
            }
        }).sheet()
                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                .headRowNumber(1)
                .doRead();

    }


}
