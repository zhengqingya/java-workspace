package com.zhengqing.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * <p> selenium测试 </p>
 *
 * @author zhengqingya
 * @description 下载 chromedriver.exe http://chromedriver.storage.googleapis.com/index.html
 * eg: http://chromedriver.storage.googleapis.com/index.html?path=111.0.5563.19
 * @date 2023/3/24 15:57
 */
@Slf4j
public class AppTest {

    @Test
    public void test() throws Exception {
        // 浏览器驱动
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // 1、打开谷歌浏览器
        ChromeDriver browser = new ChromeDriver();
        // 2、访问网址
        browser.get("http://www.baidu.com");
        // 3、搜索框中输入程序员郑清
        browser.findElement(By.id("kw")).sendKeys("程序员郑清");
        // 4、点击搜索按钮
        browser.findElement(By.id("su")).click();
        // 5、滑到底部
        browser.executeScript("window.scrollTo(0,1000)");

        // 休息3秒
        Thread.sleep(3000);

        // 6、点击下一页按钮
        WebElement nextBtn = browser.findElement(By.xpath("//a[@class='n']"));
        System.out.println(nextBtn.getText());
        nextBtn.click();

        // 休息3秒
//        Thread.sleep(3000);
//        // 关闭
//        browser.close();
//        // 退出
//        browser.quit();
    }

}
