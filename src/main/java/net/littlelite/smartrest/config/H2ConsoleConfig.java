/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ConsoleConfig
{
    @Value("${spring.h2.console.path:/h2}")
    private String h2ConsolePath;

    @Bean
    @ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServletRegistration()
    {
        String basePath = h2ConsolePath.startsWith("/") ? h2ConsolePath : "/" + h2ConsolePath;
        if (basePath.endsWith("/"))
        {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        ServletRegistrationBean<JakartaWebServlet> registration =
                new ServletRegistrationBean<>(new JakartaWebServlet(), basePath + "/*", basePath);
        registration.setName("H2Console");
        return registration;
    }
}
