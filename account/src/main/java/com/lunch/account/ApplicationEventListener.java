package com.lunch.account;

import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.LogUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.*;

/**
 * 应用声明周期监视器
 */
public class ApplicationEventListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 在这里可以监听到Spring Boot的生命周期
        if (event instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量
            LogNewUtils.info("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) { // 初始化完成
            LogNewUtils.info("初始化完成");
        } else if (event instanceof ContextRefreshedEvent) { // 应用刷新
            LogNewUtils.info("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {// 应用已启动完成
            LogNewUtils.info("应用已启动完成");
        } else if (event instanceof ContextStartedEvent) { // 应用启动，需要在代码动态添加监听器才可捕获
            LogNewUtils.info("应用启动，需要在代码动态添加监听器才可捕获");
        } else if (event instanceof ContextStoppedEvent) { // 应用停止
            LogNewUtils.info("应用停止");
        } else if (event instanceof ContextClosedEvent) { // 应用关闭
            LogNewUtils.info("应用关闭");
        }
    }
}
