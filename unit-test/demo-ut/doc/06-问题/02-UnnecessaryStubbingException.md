### 问题

![](./images/02-UnnecessaryStubbingException-1742957979498.png)

```shell
org.mockito.exceptions.misusing.UnnecessaryStubbingException: 
Unnecessary stubbings detected in test class: UserTest
Clean & maintainable test code requires zero unnecessary code.
Following stubbings are unnecessary (click to navigate to relevant line of code):
  1. -> at com.zhengqingya.demo.UserTest.query(UserTest.java:53)
Please remove unnecessary stubbings or use 'lenient' strictness. More info: javadoc for UnnecessaryStubbingException class.

	at org.mockito.internal.runners.StrictRunner.run(StrictRunner.java:52)
	at org.mockito.junit.MockitoJUnitRunner.run(MockitoJUnitRunner.java:163)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:69)
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
