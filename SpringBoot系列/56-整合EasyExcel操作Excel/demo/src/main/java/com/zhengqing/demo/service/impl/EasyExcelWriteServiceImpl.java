package com.zhengqing.demo.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.google.common.collect.Lists;
import com.zhengqing.demo.constant.AppConstant;
import com.zhengqing.demo.mapper.DemoMapper;
import com.zhengqing.demo.model.bo.OrderVO;
import com.zhengqing.demo.model.dto.DemoData;
import com.zhengqing.demo.model.vo.DemoListVO;
import com.zhengqing.demo.model.vo.DemoMergeData;
import com.zhengqing.demo.service.IEasyExcelWriteService;
import com.zhengqing.demo.util.EasyExcelUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
public class EasyExcelWriteServiceImpl implements IEasyExcelWriteService {

    @Resource
    private DemoMapper demoMapper;


    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 直接写即可
     */
    @Override
    public void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = AppConstant.WRITE_ROOT_DIRECTORY + "simpleWrite.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoListVO.class)
                .sheet("模板")
//                .doWrite(() -> {
//                    // TODO 分页查询数据...  这里暂模拟数据
//                    return Lists.newArrayList(
//                            DemoData.builder().string("str1").date(new Date()).doubleData(1d).build(),
//                            DemoData.builder().string("str2").date(new Date()).doubleData(2.56d).build(),
//                            DemoData.builder().string("str3").date(new Date()).doubleData(6.66d).build()
//                    );
//                });
                .doWrite(this.data());
    }

    /**
     * 合并单元格
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData} {@link DemoMergeData}
     * <p>
     * 2. 创建一个merge策略 并注册
     * <p>
     * 3. 直接写即可
     *
     * @since 2.2.0-beta1
     */
    @Override
    public void mergeWrite() {
        // 方法1 注解
        String fileName = AppConstant.WRITE_ROOT_DIRECTORY + "mergeWrite.xlsx";
        // 在DemoStyleData里面加上ContentLoopMerge注解
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoMergeData.class).sheet("模板").doWrite(this.data());

        // 方法2 自定义合并单元格策略
        fileName = AppConstant.WRITE_ROOT_DIRECTORY + "mergeWrite_2.xlsx";
        // 每隔3行会合并 把eachColumn 设置成 3 也就是我们数据的长度，所以就第一列会合并。当然其他合并策略也可以自己写
        LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(3, 0);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).registerWriteHandler(loopMergeStrategy).sheet("模板").doWrite(this.data());
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DownloadData}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @Override
    @SneakyThrows(Exception.class)
    public void download(HttpServletResponse response) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 此header解决前端下载不了文件问题
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        EasyExcel.write(response.getOutputStream(), DemoListVO.class).sheet("模板").doWrite(this.data());

    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @Override
    @SneakyThrows(Exception.class)
    public void downloadFailedUsingJson(HttpServletResponse response) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            int a = 1 / 0;
            EasyExcel.write(response.getOutputStream(), DemoListVO.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("模板")
                    .doWrite(this.data());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtil.toJsonStr(map));
        }
    }

    @Override
    @SneakyThrows(Exception.class)
    public void dynamicMergeData(HttpServletResponse response) {
        // 获取数据
        List<OrderVO> dataList = Lists.newArrayList(
                OrderVO.builder()
                        .name("测试1")
                        .type(1)
                        .productName("小汽车")
                        .productPrice(602)
                        .build(),
                OrderVO.builder()
                        .name("测试2")
                        .type(0)
                        .productName("衣服")
                        .productPrice(201)
                        .build(),
                OrderVO.builder()
                        .name("测试2")
                        .type(1)
                        .productName("裤子")
                        .productPrice(205)
                        .build()

        );
        // 生成自增序号
        for (int i = 0; i < dataList.size(); i++) {
            OrderVO item = dataList.get(i);
            item.setId(i + 1);
            item.setCreateTime(DateUtil.date().toString());
        }
        //需要合并的列
        int[] mergeColIndex = {1, 2};
        //从第二行后开始合并
        int mergeRowIndex = 2;
        EasyExcelUtil.dynamicMergeData(response, "测试动态合并单元格demo", "sheet_01", OrderVO.class, dataList, mergeColIndex, mergeRowIndex);

    }

    private List<DemoListVO> data() {
        List<DemoListVO> list = Lists.newLinkedList();
        for (int i = 0; i < 10; i++) {
            list.add(DemoListVO.builder()
                    .id(i + 1)
                    .username("test: " + i)
                    .password("password: " + i)
                    .build());
        }
        return list;
    }

}
