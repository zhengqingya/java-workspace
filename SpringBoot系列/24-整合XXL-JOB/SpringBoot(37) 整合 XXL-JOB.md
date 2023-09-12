### 一、前言

本文基于`springboot 2.4.0`整合`XXL-JOB`，实现简单的`任务执行`。

### 二、搭建任务调度中心

```shell
git clone https://gitee.com/zhengqingya/docker-compose.git
cd docker-compose/Liunx
# 运行
docker-compose -f docker-compose-xxl-job.yml -p xxl-job up -d
```

访问地址：`http://ip地址:9003`
默认登录账号密码：`admin/123456`

![](./images/20230912141306796.png)

注：此容器方式部署，需要将`执行器管理`中的`机器地址`修改为宿主机ip，不能使用`127.0.0.1`
![](./images/20230912141306829.png)


### 三、SpringBoot整合XXL-JOB

#### 1、`pom.xml`中引入依赖

```xml
<!--https://mvnrepository.com/artifact/com.xuxueli/xxl-job-core -->
<dependency>
  <groupId>com.xuxueli</groupId>
  <artifactId>xxl-job-core</artifactId>
  <version>2.3.0</version>
</dependency>
```

#### 2、`application.yml`中配置

```yml
# XXL-JOB 配置
xxl:
  job:
    admin:
      addresses: http://127.0.0.1:9003/xxl-job-admin # 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
    accessToken:  # 执行器通讯TOKEN [选填]：非空时启用；
    executor:
      appname: xxl-job-executor-sample # 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
      address: # 执行器注册 [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
      ip: # 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
      port: 9999 # 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
      logpath: /applogs/xxl-job/jobhandler # 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
      logretentiondays: 30 # 执行器日志文件保存天数 [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
```

#### 3、执行器组件配置

```java
@Slf4j
@Configuration
public class XxlJobConfig {

  @Value("${xxl.job.admin.addresses}")
  private String adminAddresses;

  @Value("${xxl.job.accessToken}")
  private String accessToken;

  @Value("${xxl.job.executor.appname}")
  private String appname;

  @Value("${xxl.job.executor.address}")
  private String address;

  @Value("${xxl.job.executor.ip}")
  private String ip;

  @Value("${xxl.job.executor.port}")
  private int port;

  @Value("${xxl.job.executor.logpath}")
  private String logPath;

  @Value("${xxl.job.executor.logretentiondays}")
  private int logRetentionDays;


  @Bean
  public XxlJobSpringExecutor xxlJobExecutor() {
    log.info(">>>>>>>>>>> xxl-job config init.");
    // 创建 XxlJobSpringExecutor 执行器
    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
    xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
    xxlJobSpringExecutor.setAppname(appname);
    xxlJobSpringExecutor.setAddress(address);
    xxlJobSpringExecutor.setIp(ip);
    xxlJobSpringExecutor.setPort(port);
    xxlJobSpringExecutor.setAccessToken(accessToken);
    xxlJobSpringExecutor.setLogPath(logPath);
    xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
    return xxlJobSpringExecutor;
  }

  /**
   * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
   *
   *      1、引入依赖：
   *          <dependency>
   *             <groupId>org.springframework.cloud</groupId>
   *             <artifactId>spring-cloud-commons</artifactId>
   *             <version>${version}</version>
   *         </dependency>
   *
   *      2、配置文件，或者容器启动变量
   *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
   *
   *      3、获取IP
   *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
   */

}
```

#### 4、任务开发示例（Bean模式）

> 开发步骤： 
> 1. 任务开发：在Spring Bean实例中，开发Job方法； 
> 2. 注解配置：为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。 
> 3. 执行日志：需要通过"XxlJobHelper.log" 打印执行日志； 
> 4. 任务结果：默认任务结果为 "成功" 状态，不需要主动设置；如有诉求，比如设置任务结果为失败，可以通过"XxlJobHelper.handleFail/handleSuccess" 自主设置任务结果；

```java
@Slf4j
@Component
public class SampleXxlJob {

  /**
   * 1、简单任务示例（Bean模式）
   */
  @XxlJob("demoJobHandler")
  public void demoJobHandler() throws Exception {
    XxlJobHelper.log("XXL-JOB, Hello World.");
    for (int i = 0; i < 5; i++) {
      XxlJobHelper.log("beat at:" + i);
      TimeUnit.SECONDS.sleep(2);
    }
    // default success
  }


  /**
   * 2、分片广播任务
   */
  @XxlJob("shardingJobHandler")
  public void shardingJobHandler() throws Exception {

    // 分片参数
    int shardIndex = XxlJobHelper.getShardIndex();
    int shardTotal = XxlJobHelper.getShardTotal();

    XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

    // 业务逻辑
    for (int i = 0; i < shardTotal; i++) {
      if (i == shardIndex) {
        XxlJobHelper.log("第 {} 片, 命中分片开始处理", i);
      } else {
        XxlJobHelper.log("第 {} 片, 忽略", i);
      }
    }

  }


  /**
   * 3、命令行任务
   */
  @XxlJob("commandJobHandler")
  public void commandJobHandler() throws Exception {
    String command = XxlJobHelper.getJobParam();
    int exitValue = -1;

    BufferedReader bufferedReader = null;
    try {
      // command process
      ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.command(command);
      processBuilder.redirectErrorStream(true);

      Process process = processBuilder.start();
      //Process process = Runtime.getRuntime().exec(command);

      BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
      bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

      // command log
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        XxlJobHelper.log(line);
      }

      // command exit
      process.waitFor();
      exitValue = process.exitValue();
    } catch (Exception e) {
      XxlJobHelper.log(e);
    } finally {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
    }

    if (exitValue == 0) {
      // default success
    } else {
      XxlJobHelper.handleFail("command exit value(" + exitValue + ") is failed");
    }

  }


  /**
   * 4、跨平台Http任务 参数示例： "url: http://www.baidu.com\n" + "method: get\n" + "data: content\n";
   */
  @XxlJob("httpJobHandler")
  public void httpJobHandler() throws Exception {

    // param parse
    String param = XxlJobHelper.getJobParam();
    if (param == null || param.trim().length() == 0) {
      XxlJobHelper.log("param[" + param + "] invalid.");

      XxlJobHelper.handleFail();
      return;
    }

    String[] httpParams = param.split("\n");
    String url = null;
    String method = null;
    String data = null;
    for (String httpParam : httpParams) {
      if (httpParam.startsWith("url:")) {
        url = httpParam.substring(httpParam.indexOf("url:") + 4).trim();
      }
      if (httpParam.startsWith("method:")) {
        method = httpParam.substring(httpParam.indexOf("method:") + 7).trim().toUpperCase();
      }
      if (httpParam.startsWith("data:")) {
        data = httpParam.substring(httpParam.indexOf("data:") + 5).trim();
      }
    }

    // param valid
    if (url == null || url.trim().length() == 0) {
      XxlJobHelper.log("url[" + url + "] invalid.");

      XxlJobHelper.handleFail();
      return;
    }
    if (method == null || !Arrays.asList("GET", "POST").contains(method)) {
      XxlJobHelper.log("method[" + method + "] invalid.");

      XxlJobHelper.handleFail();
      return;
    }
    boolean isPostMethod = method.equals("POST");

    // request
    HttpURLConnection connection = null;
    BufferedReader bufferedReader = null;
    try {
      // connection
      URL realUrl = new URL(url);
      connection = (HttpURLConnection) realUrl.openConnection();

      // connection setting
      connection.setRequestMethod(method);
      connection.setDoOutput(isPostMethod);
      connection.setDoInput(true);
      connection.setUseCaches(false);
      connection.setReadTimeout(5 * 1000);
      connection.setConnectTimeout(3 * 1000);
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
      connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

      // do connection
      connection.connect();

      // data
      if (isPostMethod && data != null && data.trim().length() > 0) {
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.write(data.getBytes("UTF-8"));
        dataOutputStream.flush();
        dataOutputStream.close();
      }

      // valid StatusCode
      int statusCode = connection.getResponseCode();
      if (statusCode != 200) {
        throw new RuntimeException("Http Request StatusCode(" + statusCode + ") Invalid.");
      }

      // result
      bufferedReader = new BufferedReader(
          new InputStreamReader(connection.getInputStream(), "UTF-8"));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        result.append(line);
      }
      String responseMsg = result.toString();

      XxlJobHelper.log(responseMsg);

      return;
    } catch (Exception e) {
      XxlJobHelper.log(e);

      XxlJobHelper.handleFail();
      return;
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
        if (connection != null) {
          connection.disconnect();
        }
      } catch (Exception e2) {
        XxlJobHelper.log(e2);
      }
    }

  }

  /**
   * 5、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
   */
  @XxlJob(value = "demoJobHandler2", init = "init", destroy = "destroy")
  public void demoJobHandler2() throws Exception {
    XxlJobHelper.log("XXL-JOB, Hello World.");
  }

  public void init() {
    log.info("init");
  }

  public void destroy() {
    log.info("destory");
  }

}
```

编写完成之后，运行项目

#### 5、任务调度中心 - 任务执行

小编这里直接使用默认示例执行一次
![](./images/20230912141306867.png)
查看调度日志
![](./images/20230912141306898.png)

查看执行日志
![](./images/20230912141306936.png)

### 本文案例demo源码

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

---

> 今日分享语句：
> 注意你的思想，它会变成你的言语；注意你的言语，它会变成你的行动；注意你的行动，它会变成你的习惯；注意你的习惯，它会变成你的性格；注意你的性格，它会变成你的命运。
