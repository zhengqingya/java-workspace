package com.zhengqing.demo.modules.system.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhengqing.demo.modules.common.api.BaseController;
import com.zhengqing.demo.modules.common.dto.output.ApiResult;
import com.zhengqing.demo.modules.system.entity.SysLog;
import com.zhengqing.demo.modules.system.service.ILogService;
import com.zhengqing.demo.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p> 测试 - 控制层 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/27 12:44
 */
@RestController
@RequestMapping("/api")
@Api(description = "测试-接口")
public class IndexController extends BaseController {

    private final String key = "sysLog";
    private final String key2 = "sysLog2";

    @Autowired
    private ILogService logService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping(value = "/saveData", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存数据", httpMethod = "POST", response = ApiResult.class)
    public ApiResult saveData(@RequestBody SysLog sysLog) {
        List<SysLog> sysLogList = logService.selectList(null);
        redisUtil.set(key, JSON.toJSONString(sysLogList));

        redisTemplate.opsForValue().set(key2, sysLogList);
        return ApiResult.ok("SUCCESS");
    }

    @GetMapping(value = "/getData", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取数据", httpMethod = "GET", response = ApiResult.class)
    public ApiResult getData() {
        String sysLogList = redisUtil.get(key);
        List<SysLog> result = JSONArray.parseArray(sysLogList, SysLog.class);

        List<SysLog> list = (List<SysLog>) redisTemplate.opsForValue().get(key2);
        System.out.println(list);
        return ApiResult.ok("SUCCESS", result);
    }

    @GetMapping(value = "/index", produces = "application/json;charset=utf-8")
    public ApiResult index() {
        return ApiResult.ok("Hello World ~ ");
    }

}
