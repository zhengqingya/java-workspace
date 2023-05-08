package com.zhengqing.demo;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * PageOffice测试demo Controller
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/6/21 21:18
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class DemoController {

    @Value("${pageoffice.posyspath}")
    private String poSysPath;

    @Value("${pageoffice.popassword}")
    private String poPassWord;

    @Value("${pageoffice.docpath}")
    private String docPath;

    /**
     * 被`@PostConstruct`修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init()方法之前执行。
     */
    @PostConstruct
    public void init() {
        poSysPath = Constants.PROJECT_ROOT_DIRECTORY + poSysPath;
        docPath = Constants.PROJECT_ROOT_DIRECTORY + docPath;
    }

    @RequestMapping("/hello")
    public String hello() {
        log.info("hello ...");
        return "HelloWorld~";
    }

    @RequestMapping(value = "/word", method = RequestMethod.GET)
    public ModelAndView showWord(HttpServletRequest request, Map<String, Object> map) {
        log.info("编辑word ...");
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        // 设置服务页面
        poCtrl.setServerPage("/poserver.zz");
        // 添加自定义保存按钮
        poCtrl.addCustomToolButton("保存", "Save", 1);
        // 添加自定义盖章按钮
        poCtrl.addCustomToolButton("盖章", "AddSeal", 2);
        // 拿到请求前缀做拼接保存文件方法
        String requestApiPrefix = request.getServletPath().replace("/word", "");
        // 设置处理文件保存的请求方法
        poCtrl.setSaveFilePage(requestApiPrefix + "/save");
        // 打开word
        poCtrl.webOpen("file://" + docPath + Constants.FILE_NAME_WORD, OpenModeType.docAdmin, "张三");
        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));

        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView showExcel(HttpServletRequest request, Map<String, Object> map) {
        log.info("编辑excel ...");
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        // 设置服务页面
        poCtrl.setServerPage("/poserver.zz");
        // 添加自定义保存按钮
        poCtrl.addCustomToolButton("保存", "Save", 1);
        // 添加自定义盖章按钮
        poCtrl.addCustomToolButton("盖章", "AddSeal", 2);
        // 拿到请求前缀做拼接保存文件方法
        String requestApiPrefix = request.getServletPath().replace("/excel", "");
        // 设置处理文件保存的请求方法
        poCtrl.setSaveFilePage(requestApiPrefix + "/save");
        // 打开word
        poCtrl.webOpen("file://" + docPath + Constants.FILE_NAME_EXCEL, OpenModeType.xlsNormalEdit, "张三");
        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));

        ModelAndView mv = new ModelAndView("Excel");
        return mv;
    }

    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
        log.info("保存文件 ...");
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile(docPath + fs.getFileName());
        fs.close();
    }

    /**
     * 添加PageOffice的服务器端授权程序Servlet（必须）
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
        // 设置PageOffice注册成功后,license.lic文件存放的目录
        poserver.setSysPath(poSysPath);
        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
        srb.addUrlMappings("/poserver.zz");
        srb.addUrlMappings("/posetup.exe");
        srb.addUrlMappings("/pageoffice.js");
        srb.addUrlMappings("/jquery.min.js");
        srb.addUrlMappings("/pobstyle.css");
        srb.addUrlMappings("/sealsetup.exe");
        return srb;
    }

    /**
     * 添加印章管理程序Servlet（可选）
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean2() {
        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal =
            new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();
        // 设置印章管理员admin的登录密码
        adminSeal.setAdminPassword(poPassWord);
        // 设置印章数据库文件poseal.db存放的目录
        adminSeal.setSysPath(poSysPath);
        ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);
        srb.addUrlMappings("/adminseal.zz");
        srb.addUrlMappings("/sealimage.zz");
        srb.addUrlMappings("/loginseal.zz");
        return srb;
    }
}
