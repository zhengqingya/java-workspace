# spy

Mockito 的 `spy` 方法用于创建一个被监视的（spied）对象。
这个对象是真实的对象，但你可以监视它的方法调用，并可以选择性地模拟某些方法的行为。

以下是 `spy` 的一些常见用法：

- **监视真实对象**：当你想要监视一个真实对象的方法调用，但又不想完全模拟它时，可以使用 `spy`。
- **部分模拟**：你可以选择性地模拟对象的某些方法，而让其他方法保持真实的行为。

### 基本用法

```
import static org.mockito.Mockito.*;

// 创建一个真实的对象
List<String> realList = new ArrayList<>();

// 创建一个被监视的对象
List<String> spyList = spy(realList);

// 模拟 spyList 的 size() 方法
when(spyList.size()).thenReturn(100);

// 使用 spyList
spyList.add("one");
spyList.add("two");

// 验证 add 方法被调用了两次
verify(spyList, times(2)).add(anyString());

// 输出 size() 方法的结果，返回的是模拟的值
System.out.println(spyList.size());  // 输出 100

// 输出真实的方法调用结果
System.out.println(spyList.get(0));  // 输出 "one"
```

### 注意事项

1. **真实方法调用**：当你调用 `spy` 对象的方法时，真实的方法会被调用，除非你使用 `when` 来模拟这个方法。
2. **异常处理**：如果真实的方法抛出异常，`spy` 对象也会抛出相同的异常。
3. **私有方法**：`spy` 不能直接监视私有方法，但你可以通过反射来实现。

### 使用场景

- **测试继承类**：当你需要测试一个继承类的行为，但又不想完全模拟父类的行为时。
- **测试框架集成**：当你需要在测试中监视框架提供的对象时。

通过合理使用 `spy`，你可以更灵活地控制测试中的对象行为，从而编写更有效的单元测试。

