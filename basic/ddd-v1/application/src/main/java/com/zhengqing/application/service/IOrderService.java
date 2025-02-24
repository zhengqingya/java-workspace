package com.zhengqing.application.service;


import com.zhengqing.application.resp.OrderDetailResp;

public interface IOrderService {

    OrderDetailResp detail(Long id);

}
