package com.zhengqing.spring.boot;

import com.zhengqing.spring.io.PropertyResolver;
import com.zhengqing.spring.util.ClassPathUtils;
import com.zhengqing.spring.web.ContextLoaderInitializer;
import com.zhengqing.spring.webmvc.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
import java.util.Set;

@Slf4j
public class Application {

    static final String CONFIG_APP_YAML = "/application.yml";
    static final String CONFIG_APP_PROP = "/application.properties";

    public static void run(String webDir, String baseDir, Class<?> configClass, String... args) throws Exception {
        new Application().start(webDir, baseDir, configClass, args);
    }

    public void start(String webDir, String baseDir, Class<?> configClass, String... args) throws Exception {
        printBanner();

        // start info
        final long startTime = System.currentTimeMillis();
        final int javaVersion = Runtime.version().feature();
        final long pid = ManagementFactory.getRuntimeMXBean().getPid();
        final String user = System.getProperty("user.name");
        final String pwd = Paths.get("").toAbsolutePath().toString();
        log.info("Starting {} using Java {} with PID {} (started by {} in {})", configClass.getSimpleName(), javaVersion, pid, user, pwd);

        // 读取application.yml配置
        var propertyResolver = WebUtils.createPropertyResolver();
        // 创建Tomcat服务器
        var server = startTomcat(webDir, baseDir, configClass, propertyResolver);

        // started info
        final long endTime = System.currentTimeMillis();
        final String appTime = String.format("%.3f", (endTime - startTime) / 1000.0);
        final String jvmTime = String.format("%.3f", ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0);
        log.info("Started {} in {} seconds (process running for {})", configClass.getSimpleName(), appTime, jvmTime);

        // 等待服务器结束
        server.await();
    }

    protected Server startTomcat(String webDir, String baseDir, Class<?> configClass, PropertyResolver propertyResolver) throws Exception {
        int port = propertyResolver.getProperty("${server.port:8080}", int.class);
        log.info("starting Tomcat at port {}...", port);
        // 实例化Tomcat Server
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        // 设置Connector
        tomcat.getConnector().setThrowOnFailure(true);
        // 添加一个默认的Webapp，挂载在'/'
        Context ctx = tomcat.addWebapp("", new File(webDir).getAbsolutePath());
        // 设置应用程序的目录
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File(baseDir).getAbsolutePath(), "/"));
        ctx.setResources(resources);
        // 设置ServletContainerInitializer监听器
        ctx.addServletContainerInitializer(new ContextLoaderInitializer(configClass, propertyResolver), Set.of());
        // 启动服务器
        tomcat.start();
        log.info("Tomcat started at port {}...", port);
        return tomcat.getServer();
    }

    protected void printBanner() {
        String banner = ClassPathUtils.readString("/banner.txt");
        banner.lines().forEach(System.out::println);
    }
}
