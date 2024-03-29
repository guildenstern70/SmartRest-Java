/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SmartRest implements CommandLineRunner
{
	public static final String VERSION = "0.1.0";

	private final Logger logger = LoggerFactory.getLogger(SmartRest.class);

	private final Environment environment;

	@Autowired
	public SmartRest(Environment environment)
	{
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(SmartRest.class, args);
	}

	@Override
	public void run(String... args)
	{
		String jvm = " (JVM " + System.getProperty("java.version") + ")";
		String runningUrl = "http://localhost:" + this.environment.getProperty("local.server.port");
		logger.info("*******************************************");
		logger.info("  SmartREST - Java Edition - v." + VERSION);
		logger.info("  Running on " + runningUrl + jvm);
		logger.info("*******************************************");
	}
}
