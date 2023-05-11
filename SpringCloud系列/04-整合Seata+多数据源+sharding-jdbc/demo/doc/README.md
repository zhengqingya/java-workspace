# 说明

> 下面为熟练后的简版快速使用文档

### tips

1. 测试环境中导入数据库[test.sql](./test.sql)
2. Seata服务端自行部署

### Sharding整合Seata

https://shardingsphere.apache.org/document/legacy/4.x/document/cn/features/transaction/principle/base-transaction-seata/

### 多数据源

1. 配置 `spring.datasource.dynamic.seata=true`
2. 配置 `seata.enable-auto-data-source-proxy=false`
3. 代码声明
    ```
    @GlobalTransactional(rollbackFor = Exception.class)
    @DS("db-test")
    ```

### Sharding+Seata+多数据源 整合

#### 分库分表

##### 父服务声明（主业务调用方）

> tips:
> 网上有资料说这里的2个注解不能同时使用，但在我测试看是必须一起使用！
> 前期也是乖乖娃儿，听了网上的，一直没一起使用，导致整合浪费半天时间...
> 当然也有可能是因为版本问题差异...

```
@GlobalTransactional(rollbackFor = Exception.class)
@ShardingTransactionType(TransactionType.BASE)
```

##### 子服务声明

```
@Transactional(rollbackFor = Exception.class)
@ShardingTransactionType(TransactionType.BASE)
```

#### 多数据源

单库单表

```
@GlobalTransactional(rollbackFor = Exception.class)
@DS("db-test")
```

---

### undo_log 表中的回滚信息查看

```sql
SELECT CONVERT(rollback_info USING utf8)
FROM undo_log LIMIT 2;
```

eg:

```
{
    "@class":"io.seata.rm.datasource.undo.BranchUndoLog",
    "xid":"172.16.16.88:8091:144505327427725328",
    "branchId":144505327427725332,
    "sqlUndoLogs":[
        "java.util.ArrayList",
        [
            {
                "@class":"io.seata.rm.datasource.undo.SQLUndoLog",
                "sqlType":"INSERT",
                "tableName":"t_user1",
                "beforeImage":{
                    "@class":"io.seata.rm.datasource.sql.struct.TableRecords$EmptyTableRecords",
                    "tableName":"t_user1",
                    "rows":[
                        "java.util.ArrayList",
                        [

                        ]
                    ]
                },
                "afterImage":{
                    "@class":"io.seata.rm.datasource.sql.struct.TableRecords",
                    "tableName":"t_user1",
                    "rows":[
                        "java.util.ArrayList",
                        [
                            {
                                "@class":"io.seata.rm.datasource.sql.struct.Row",
                                "fields":[
                                    "java.util.ArrayList",
                                    [
                                        {
                                            "@class":"io.seata.rm.datasource.sql.struct.Field",
                                            "name":"create_time",
                                            "keyType":"NULL",
                                            "type":93,
                                            "value":[
                                                "java.time.LocalDateTime",
                                                1683788595000
                                            ]
                                        },
                                        {
                                            "@class":"io.seata.rm.datasource.sql.struct.Field",
                                            "name":"password",
                                            "keyType":"NULL",
                                            "type":12,
                                            "value":"123456"
                                        },
                                        {
                                            "@class":"io.seata.rm.datasource.sql.struct.Field",
                                            "name":"user_id",
                                            "keyType":"PRIMARY_KEY",
                                            "type":-5,
                                            "value":[
                                                "java.math.BigInteger",
                                                1656555550344872000
                                            ]
                                        },
                                        {
                                            "@class":"io.seata.rm.datasource.sql.struct.Field",
                                            "name":"username",
                                            "keyType":"NULL",
                                            "type":12,
                                            "value":"test"
                                        }
                                    ]
                                ]
                            }
                        ]
                    ]
                }
            }
        ]
    ]
}
```

