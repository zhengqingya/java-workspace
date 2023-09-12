### 一、前言

AviatorScript 是一门`高性能`、`轻量级`寄宿于 `JVM` 之上的`脚本语言`。

1. GitHub地址：[https://github.com/killme2008/aviator](https://github.com/killme2008/aviator)
2. AviatorScript 文档： [https://www.yuque.com/boyan-avfmj/aviatorscript](https://www.yuque.com/boyan-avfmj/aviatorscript)

![](./images/20230912144041547.png)

### 二、SpringBoot整合Aviator入门

> 温馨小提示：本文demo基于springboot`2.3.1.RELEASE`版本

#### 1、`pom.xml`中引入`aviator`依赖

```xml
<!-- 表达式引擎 Aviator -->
<!-- https://mvnrepository.com/artifact/com.googlecode.aviator/aviator -->
<dependency>
  <groupId>com.googlecode.aviator</groupId>
  <artifactId>aviator</artifactId>
  <version>5.1.2</version>
</dependency>
```

#### 2、体验

```java
public class TestAviator {

    public static void main(String[] args) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("算术表达式【1+1】： " + AviatorEvaluator.execute("1+1"));
        System.out.println("逻辑表达式【1==1】： " + AviatorEvaluator.execute("1==1"));
        System.out.println("三元表达式【1==1 ? '对' : '错'】： " + AviatorEvaluator.execute("1==1 ? '对' : '错'"));
        System.out.println("函数调用【6的3次方】： " + AviatorEvaluator.execute("math.pow(6,3)"));
        System.out.println("-----------------------------------------------------------------");
    }

}
```

运行效果：
![](./images/20230912144041631.png)


---

### 本文案例demo源码地址

[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

---

> 今日分享语句：
> 起点低怕什么，大不了加倍努力。人生就像一场马拉松比赛，拼的不是起点，而是坚持的耐力和成长的速度。只要努力不止，进步也会不止。
