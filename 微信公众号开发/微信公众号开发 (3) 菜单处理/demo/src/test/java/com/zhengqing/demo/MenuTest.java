package com.zhengqing.demo;

import com.alibaba.fastjson.JSON;
import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.weixin.model.WeixinResponseResult;
import com.zhengqing.demo.modules.weixin.model.menu.*;
import com.zhengqing.demo.modules.weixin.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p> 菜单接口测试$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16$ 16:40$
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class MenuTest {

    @Autowired
    private IMenuService menuService;

    @Test // 查询菜单
    public void getMenu() {
        Object menu = menuService.getMenu(Constants.ACCESS_TOKEN);
        log.info("======================================== \n" + JSON.toJSONString(menu));
    }

    @Test // 删除菜单
    public void deleteMenu() {
        WeixinResponseResult result = menuService.deleteMenu(Constants.ACCESS_TOKEN);
        log.info("======================================== \n" + result);
    }

    @Test // 创建菜单
    public void createMenu() {
        WeixinResponseResult result = menuService.createMenu(createMenuTree(), Constants.ACCESS_TOKEN);
        log.info("======================================== \n" + result);
    }

    /**
     * 菜单数据
     */
    private Menu createMenuTree() {
        // 链接式菜单
        ViewButton btn11 = new ViewButton();
        btn11.setName("CSDN");
        btn11.setUrl("https://zhengqing.blog.csdn.net/");

        ViewButton btn12 = new ViewButton();
        btn12.setName("个人博客");
        btn12.setUrl("http://zhengqingya.gitee.io/blog/");

        // 点击式菜单
        ClickButton mainBtn2 = new ClickButton();
        mainBtn2.setName("点我吖");
        mainBtn2.setKey("hello");

        ViewButton btn31 = new ViewButton();
        btn31.setName("码云");
        btn31.setUrl("https://gitee.com/zhengqingya/projects");

        ViewButton btn32 = new ViewButton();
        btn32.setName("GitHub");
        btn32.setUrl("https://github.com/zhengqingya?tab=repositories");

        // 含二级菜单的一级菜单
        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("博客");
        mainBtn1.setSub_button(new ViewButton[]{btn11, btn12});

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("仓库");
        mainBtn3.setSub_button(new ViewButton[]{btn31, btn32});

        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});

        return menu;
    }

}
