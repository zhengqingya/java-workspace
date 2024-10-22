package com.zhengqing.spring.jdbc.tx;


import com.zhengqing.spring.annotation.Transactional;
import com.zhengqing.spring.aop.AnnotationProxyBeanPostProcessor;

public class TransactionalBeanPostProcessor extends AnnotationProxyBeanPostProcessor<Transactional> {

}
