### 问题

```shell
org.mockito.exceptions.misusing.NotAMockException: Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of class java.lang.Class!

	at org.mockito.internal.runners.StrictRunner.run(StrictRunner.java:52)
	at org.mockito.junit.MockitoJUnitRunner.run(MockitoJUnitRunner.java:163)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:115)
	at org.junit.vintage.engine.execution.RunnerExecutor.execute(RunnerExecutor.java:40)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
	at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
	at java.util.Iterator.forEachRemaining(Iterator.java:116)
	at java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1801)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	at org.junit.vintage.engine.VintageTestEngine.executeAllChildren(VintageTestEngine.java:80)
	at org.junit.vintage.engine.VintageTestEngine.execute(VintageTestEngine.java:71)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:107)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:88)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:54)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:67)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:52)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:114)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:86)
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.execute(DefaultLauncherSession.java:86)
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(SessionPerRequestLauncher.java:53)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:57)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater$1.execute(IdeaTestRunner.java:38)
	at com.intellij.rt.execution.junit.TestsRepeater.repeat(TestsRepeater.java:11)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:35)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:232)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:55)
```

### 解决

删除 `@RunWith(MockitoJUnitRunner.class)`

使用:

```
@Before
public void init() {
    MockitoAnnotations.openMocks(this);
}
```

---

### 其它说明

`@RunWith(MockitoJUnitRunner.class)` 是 Mockito 框架提供的一个 JUnit 运行器注解，其主要作用如下：

1. **自动初始化带有 `@Mock` 注解的字段**  
   当使用 `@Mock` 注解声明模拟对象时，`MockitoJUnitRunner` 会在测试运行之前自动初始化这些对象，无需手动调用 `MockitoAnnotations.initMocks(this)`。
2. **简化测试代码**  
   使用 `MockitoJUnitRunner` 可以减少手动初始化 Mockito 环境的工作量，使测试代码更加简洁。
3. **支持 Mockito 的功能**  
   它为测试类提供了完整的 Mockito 支持，例如模拟行为、验证交互等。

#### 示例代码

```java

@RunWith(MockitoJUnitRunner.class)
public class ExampleTest {

    @Mock
    private SomeService someService;

    @Test
    public void testMockBehavior() {
        // 直接使用 someService，无需手动初始化
        when(someService.doSomething()).thenReturn("Mocked Result");

        String result = someService.doSomething();
        assertEquals("Mocked Result", result);
    }
}
```

#### 注意事项

从上下文中的问题可以看出，`MockitoJUnitRunner` 在某些场景下可能会导致兼容性问题（例如与 JUnit 5 或其他运行器冲突）。
因此，现代推荐的做法是使用 `MockitoAnnotations.openMocks(this)` 来替代 `@RunWith(MockitoJUnitRunner.class)`，以避免潜在的问题。
