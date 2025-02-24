package com.zhengqing.infrastructure.rpc;

import com.zhengqing.domain.user.rpc.IUmsUserRpcApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UmsUserRpcApi implements IUmsUserRpcApi {

    @Override
    public String getNameById(Long id) {
        // 查询rpc接口数据...
        return "";
    }

}
