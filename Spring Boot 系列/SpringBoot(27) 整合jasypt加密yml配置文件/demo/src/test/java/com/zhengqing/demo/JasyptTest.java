package com.zhengqing.demo;

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;

/**
 * <p> jasypt 加密/解密 测试类$ </p>
 *
 * @author : zhengqing
 * @description : 【 注：每次加密后的密码都不同，但根据密钥都能解析成原本的密码 】
 * @date : 2020/4/25$ 12:12$
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoApplication.class)
public class JasyptTest {

    @Autowired
    StringEncryptor jasyptStringEncryptor;

    @Test
    public void encrypt() throws Exception {
        System.out.println("加密： " + jasyptStringEncryptor.encrypt("root"));
    }

    @Test
    public void decrypt() throws Exception {
        System.out.println("解密： " + jasyptStringEncryptor.decrypt("N/+f2B9SznK4MUDSp24Upw=="));
    }

    // ================ ↓↓↓↓↓↓ 下面为无需加载spring容器方式 ↓↓↓↓↓↓ ================

    @Test
    public void test() {
        // 对应配置文件中配置的加密密钥
        System.setProperty("jasypt.encryptor.password", "zhengqing");
        StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
        System.out.println("加密： " + stringEncryptor.encrypt("root"));
        System.out.println("解密： " + stringEncryptor.decrypt("N/+f2B9SznK4MUDSp24Upw=="));
    }

}
