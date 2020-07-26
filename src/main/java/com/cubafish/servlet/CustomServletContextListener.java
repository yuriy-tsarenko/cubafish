package com.cubafish.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CustomServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContextListener was created!");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContextListener was destroyed!");
    }
}
