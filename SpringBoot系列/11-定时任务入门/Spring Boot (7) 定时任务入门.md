### 一、前言

###### 创建定时任务方式以及区别：

1. 基于注解 `@Scheduled` -> 修改执行周期后需要重启应用才能生效
2. 基于接口 `SchedulingConfigurer`  -> 从数据库中读取指定时间来动态执行定时任务

### 二、基于注解 `@Scheduled`

```java
/**
 *  <p>静态定时任务（基于注解）</p>
 *
 * @description : @Scheduled 除了支持灵活的参数表达式cron之外，还支持简单的延时操作，例如 fixedDelay ，fixedRate 填写相应的毫秒数即可。
*                 缺点: 当我们调整了执行周期的时候，需要重启应用才能生效。 -> 为了达到实时生效的效果，可以使用接口来完成定时任务。
 * @author : zhengqing
 * @date : 2019/11/4 18:10
 */
@Configuration //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling // 2.开启定时任务
public class ScheduleConfig {
    /**
     * 3.添加定时任务
     * Cron表达式参数分别表示：
     *           秒（0~59） 例如0/5表示每5秒
     *           分（0~59）
     *           时（0~23）
     *           月的某天（0~31） 需计算
     *           月（0~11）
     *           周几（ 可填1-7 或 SUN/MON/TUE/WED/THU/FRI/SAT）
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        System.err.println("执行定时任务1: " + LocalDateTime.now());
    }
}

```


### 三、基于接口 `SchedulingConfigurer`

温馨小提示：这里采用了mybatis持久化框架去拉取数据库配置哦~  具体配置可查看文末案例源码

###### 项目依赖配置：

```xml
	<properties>
        <java.version>1.8</java.version>
        <mybatis-plus-boot-starter.version>2.2.0</mybatis-plus-boot-starter.version>
        <mysql.version>5.1.40</mysql.version>
        <commons-lang3.version>3.6</commons-lang3.version>
        <hutool-all.version>4.6.2</hutool-all.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- mybatis-plus begin =================================== -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus-boot-starter.version}</version>
        </dependency>
        <!-- mybatis-plus end -->

        <!-- ========================= 数据库相关 ========================== -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        <!-- 阿里数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.18</version>
        </dependency>

        <!-- ========================= 常用库依赖 ========================== -->
        <!-- lombok插件 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- Hutool工具类 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool-all.version}</version>
        </dependency>
        <!-- StringUtils工具类 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>${commons-lang3.version}</version>
        </dependency>
    </dependencies>
```

动态定时任务：

```java
/**
 *  <p> 动态定时任务（基于接口） </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/11/4 18:40
 */
@Configuration
@EnableScheduling
public class ScheduleConfigDynamic implements SchedulingConfigurer {

    @Autowired
    CronMapper cronMapper;

    /**
     * 执行定时任务: 可打开Navicat进行动态修改执行周期  ex:将执行周期修改为每1秒执行一次
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 添加TriggerTask -> 目的: 循环读取我们在数据库设置好的执行周期，以及执行相关定时任务的内容
        taskRegistrar.addTriggerTask(
                // 1.添加任务内容(Runnable)
                () -> System.out.println("执行定时任务2: " + LocalDateTime.now().toLocalTime()),
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    // 2.1 从数据库获取执行周期
                    String cron = cronMapper.getCron();
                    // 2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        // Omitted Code ..
                    }
                    // 2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

}
```

现在我们可以启动项目，修改数据库配置信息查看定时任务效果哦~

如下配置是每秒执行一次：
![](./images/20230912141304681.png)
![](./images/20230912141304716.png)

###### 案例源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)



参考教程：[https://www.jianshu.com/p/d160f2536de7?utm_source=tuicool&utm_medium=referral](https://www.jianshu.com/p/d160f2536de7?utm_source=tuicool&utm_medium=referral)

