package com.zhengqing.spring.webmvc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    boolean processed;
    Object returnObject;
}
