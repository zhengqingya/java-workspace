package com.zhengqing.spring.io;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyExpr {
    String key;
    String defaultValue;
}
