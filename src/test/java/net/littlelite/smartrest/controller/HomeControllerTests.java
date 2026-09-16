/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class HomeControllerTests
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("GET / returns 200 OK with home view and model attributes")
    void testHomePage() throws Exception
    {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("appName", "appVersion", "javaVersion", "databaseUrl", "swaggerUrl", "healthUrl", "apiDocsUrl"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("SmartREST")))
                .andExpect(content().string(containsString("Explore APIs")))
                .andExpect(content().string(containsString("/swagger-ui/index.html")))
                .andExpect(content().string(containsString("Health")))
                .andExpect(content().string(containsString("/actuator/health")))
                .andExpect(content().string(containsString("Download Open API")))
                .andExpect(content().string(containsString("/v3/api-docs")))
                .andExpect(content().string(containsString("Tech Stack")))
                .andExpect(content().string(containsString("Usage & Quick Start")));
    }

    @Test
    @DisplayName("GET /home returns 200 OK with home view")
    void testHomeAliasPage() throws Exception
    {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
