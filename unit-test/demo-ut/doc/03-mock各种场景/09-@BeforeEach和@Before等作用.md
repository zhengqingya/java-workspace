# @BeforeEach 和 @Before 等区别以及使用场景

在JUnit测试框架中，`@Before`、`@BeforeEach` 等注解用于定义测试生命周期中的初始化或清理操作。

以下是各注解的区别及使用场景的详细说明：

### **1. 注解对应关系（JUnit 4 vs JUnit 5）**

| **JUnit 4**    | **JUnit 5**   | 作用                  |
|:---------------|:--------------|:--------------------|
| `@Before`      | `@BeforeEach` | 每个测试方法执行前运行         |
| `@After`       | `@AfterEach`  | 每个测试方法执行后运行         |
| `@BeforeClass` | `@BeforeAll`  | 所有测试方法执行前运行一次（静态方法） |
| `@AfterClass`  | `@AfterAll`   | 所有测试方法执行后运行一次（静态方法） |

### **2. 注解详细说明**

#### **`@BeforeEach`（JUnit 5） / `@Before`（JUnit 4）**

- **作用**：标记的方法会在每个测试方法（`@Test`）执行**之前**运行。
- **使用场景**：
    - 初始化测试所需的资源（如创建对象实例、准备测试数据）。
    - 重置测试环境状态，避免测试间的副作用。
- **示例**：
  ```java
  public class ExampleTest {
      private List<String> list;
  
      @BeforeEach // JUnit 5
      void setUp() {
          list = new ArrayList<>();
          list.add("test");
      }
  
      @Test
      void testListSize() {
          assertEquals(1, list.size());
      }
  }
  ```

#### **`@BeforeAll`（JUnit 5） / `@BeforeClass`（JUnit 4）**

- **作用**：标记的方法会在所有测试方法执行**之前**运行一次。方法必须是静态的（`static`）。
- **使用场景**：
    - 初始化全局资源（如数据库连接、文件句柄）。
    - 执行耗时操作（如加载配置文件）。
- **示例**：
  ```java
  public class DatabaseTest {
      private static Connection connection;
  
      @BeforeAll // JUnit 5
      static void initDatabase() {
          connection = DriverManager.getConnection("jdbc:mysql://localhost/test");
      }
  
      @Test
      void testQuery() {
          // 使用 connection 执行查询
      }
  }
  ```

#### **`@AfterEach`（JUnit 5） / `@After`（JUnit 4）**

- **作用**：标记的方法会在每个测试方法执行**之后**运行。
- **使用场景**：
    - 清理测试产生的临时数据（如删除测试文件）。
    - 释放资源（如关闭流、回滚事务）。
- **示例**：
  ```java
  public class FileTest {
      private File tempFile;
  
      @BeforeEach
      void createTempFile() throws IOException {
          tempFile = File.createTempFile("test", ".txt");
      }
  
      @AfterEach
      void deleteTempFile() {
          tempFile.delete();
      }
  
      @Test
      void testFileExists() {
          assertTrue(tempFile.exists());
      }
  }
  ```

#### **`@AfterAll`（JUnit 5） / `@AfterClass`（JUnit 4）**

- **作用**：标记的方法会在所有测试方法执行**之后**运行一次。方法必须是静态的（`static`）。
- **使用场景**：
    - 关闭全局资源（如数据库连接、线程池）。
    - 清理持久化数据（如删除测试数据库）。
- **示例**：
  ```java
  public class ResourceCleanupTest {
      private static DatabaseConnection dbConnection;
  
      @BeforeAll
      static void setup() {
          dbConnection = new DatabaseConnection();
      }
  
      @AfterAll
      static void tearDown() {
          dbConnection.close();
      }
  
      @Test
      void testDatabaseOperation() {
          // 使用 dbConnection 执行操作
      }
  }
  ```

### **3. 关键区别与注意事项**

1. **JUnit 5 的命名更清晰**：
    - `@BeforeEach` 和 `@AfterEach` 明确表示“每个测试前后”执行，避免歧义。
    - `@BeforeAll` 和 `@AfterAll` 替代了 `@BeforeClass` 和 `@AfterClass`，强调“所有测试前后”。
2. **静态方法要求**：
    - JUnit 4 的 `@BeforeClass` 和 `@AfterClass` 要求方法为 `static`。
    - JUnit 5 的 `@BeforeAll` 和 `@AfterAll` 同样要求方法为 `static`。
3. **兼容性**：
    - JUnit 5 支持混合使用 JUnit 4 的注解，但不推荐。
    - **迁移建议**：将旧代码中的 `@Before`、`@After` 替换为 `@BeforeEach`、`@AfterEach`。
4. **错误使用示例**：
    - 在 JUnit 5 中使用 `@Before` 注解时，测试框架会忽略它，导致初始化逻辑未执行。
    - 在 `@BeforeAll` 中初始化非静态资源会导致测试失败。

### **4. 最佳实践**

1. **始终使用 JUnit 5 的注解**：
   ```java
   // 推荐
   @BeforeEach
   void setUp() { ... }
   
   // 不推荐（JUnit 4 风格）
   @Before
   public void before() { ... }
   ```
2. **分离初始化逻辑**：
    - 使用 `@BeforeEach` 初始化测试实例。
    - 使用 `@BeforeAll` 初始化全局资源。
3. **避免副作用**：
    - 确保 `@BeforeEach` 和 `@AfterEach` 不依赖测试执行顺序。
    - 每个测试方法应独立运行，互不影响。

### **总结**

- **`@BeforeEach`/`@AfterEach`**：用于每个测试方法的初始化和清理。
- **`@BeforeAll`/`@AfterAll`**：用于全局资源的初始化和清理。
- **JUnit 5 的注解更清晰、更安全**，建议优先使用。
