### 一、前言

1. [Nepxion Discovery(1) 全链路蓝绿发布](https://zhengqing.blog.csdn.net/article/details/113065844)
2. [Nepxion Discovery(2) 全链路条件蓝绿发布](https://zhengqing.blog.csdn.net/article/details/113091047)

本文将基于之前的环境来进行`全链路蓝绿发布编排建模`

### 二、环境准备

> 温馨小提示：下面所需环境一键下载地址： [https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

#### 1、下载`控制台`并导入IDEA启动 [https://github.com/Nepxion/DiscoveryPlatform](https://github.com/Nepxion/DiscoveryPlatform)

> 温馨小提示：discovery`6.6.0`版本修改为`6.5.0`版本 -> 解决相关依赖下载不了问题

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125093800782.png)


#### 2、下载`图形化桌面端` [https://github.com/Nepxion/DiscoveryUI/releases](https://github.com/Nepxion/DiscoveryUI/releases)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125093356801.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
启动
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125094419807.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125094458963.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
目前所有环境都是默认配置，可直接登录
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125094622252.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

### 三、全链路蓝绿发布编排建模（图形化桌面端操作）

#### 1、`全链路服务蓝绿发布` -> `新建` -> `新建配置` -> `确定`
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125095620607.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125095955886.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

> 下面操作示例 `全链路版本条件匹配蓝绿发布`

#### 2、`蓝绿条件`配置

| 条件 | 参数 |
|--|--|
| 蓝条件 | a==1 |
| 绿条件 | a==1&&b==2 |

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125100221826.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/202101251003520.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
#### 3、条件校验

ex: 绿条件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125100713576.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

#### 4、蓝绿编排

|  服务  |    蓝版本  | 绿版本  |  兜底版本  |
|--|--|--|--|
| discovery-guide-service-a   |   1.1     |    1.0   |    1.0    |
| discovery-guide-service-b   |   1.1     |    1.0   |    1.0    |

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125101340865.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125101433657.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

#### 5、蓝绿参数

ex：内置Header参数

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021012510160864.png)

#### 6、保存策略配置

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125101914970.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125101933584.png)
校验：访问Nacos界面查看相关规则策略是否存在 [http://127.0.0.1:8848/nacos](http://127.0.0.1:8848/nacos)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021012510210251.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

访问 [http://127.0.0.1:5001/discovery-guide-service-a/invoke/gateway](http://127.0.0.1:5001/discovery-guide-service-a/invoke/gateway) 查看配置效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125102921590.png)


> 也可以通过 `预览` -> `预览配置` -> `保存配置`
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125102205675.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

#### 7、其它

1. 对于已经存在的策略配置可通过 `打开` -> `打开配置` -> `打开远程配置` -> `确认` 载入Nacos上对应的规则策略
    ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210125102650308.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)

2. 对于已经存在的策略配置，如果想重置清除掉，点击`重置`即可
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/2021012510302615.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MjI1NTU4,size_16,color_FFFFFF,t_70)


---

> 今日分享语句：
> 笨，是一种高级的情商。
> 笨一点，就是不懂的事不要瞎做，不明白的话不要乱说。
> 笨一点，不要追根究底。
> 笨一点，不要太快放弃。
