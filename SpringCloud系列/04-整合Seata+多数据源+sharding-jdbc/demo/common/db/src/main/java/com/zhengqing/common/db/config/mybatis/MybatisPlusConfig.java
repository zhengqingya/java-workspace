package com.zhengqing.common.db.config.mybatis;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.zhengqing.common.base.context.TenantIdContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * MybatisPlus配置类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/23 9:46
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.zhengqing.*.mapper", "com.zhengqing.*.*.mapper"})
public class MybatisPlusConfig {

    /**
     * 需要设置租户ID的表
     */
    public static Set<String> TENANT_ID_TABLE = new HashSet<>();

    /**
     * 需要逻辑删除的表
     */
    public static Set<String> LOGIC_DELETE_TABLE = new HashSet<>();

    static {
        TENANT_ID_TABLE.add("t_demo2");
        LOGIC_DELETE_TABLE.add("t_demo");
    }


    /**
     * mybatis-plus分页插件
     * 文档：https://baomidou.com/pages/2976a3/#spring-boot
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /**
         * 1、mybatis-plus多租户插件
         * 文档：https://baomidou.com/pages/aef2f2/#tenantlineinnerinterceptor
         */
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(TenantIdContext.getTenantId());
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                if (!TENANT_ID_TABLE.contains(tableName)) {
                    // 不需要租户id
                    return true;
                }
                Boolean tenantIdFlag = TenantIdContext.getFlag();
                Assert.notNull(tenantIdFlag, "租户id不能为空！");
                return !tenantIdFlag;
            }
        }));

        // tips: 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor


        /**
         * 2、mybatis-plus分页插件
         * 文档：https://baomidou.com/pages/2976a3/#spring-boot
         */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
