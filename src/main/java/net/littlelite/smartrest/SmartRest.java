/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class SmartRest implements CommandLineRunner
{
    static void main(String[] args)
    {
        SpringApplication.run(SmartRest.class, args);
    }

    private final Environment environment;

    @Autowired
    public SmartRest(Environment environment)
    {
        this.environment = environment;
    }

    @Override
    public void run(String... args)
    {
        String jvm = " (JVM " + System.getProperty("java.version") + ")";
        String runningUrl = "http://localhost:" + this.environment.getProperty("local.server.port");
        log.info("*******************************************");
        log.info("  SmartREST - Java Edition");
        log.info("  Running on {}{}", runningUrl, jvm);
        log.info("*******************************************");
    }
}
