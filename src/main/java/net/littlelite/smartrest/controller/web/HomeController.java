/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.controller.web;

import lombok.RequiredArgsConstructor;
import net.littlelite.smartrest.service.AliveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController
{
    private final AliveService aliveService;

    @GetMapping({"/", "/home"})
    public String index(Model model)
    {
        model.addAttribute("appName", "SmartREST");
        model.addAttribute("appVersion", aliveService.getVersion());
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("databaseUrl", aliveService.getDatabaseUrl());
        model.addAttribute("swaggerUrl", "/swagger-ui/index.html");
        model.addAttribute("healthUrl", "/actuator/health");
        model.addAttribute("apiDocsUrl", "/v3/api-docs");
        return "home";
    }
}
