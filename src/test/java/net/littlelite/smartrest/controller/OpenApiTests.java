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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class OpenApiTests
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
    @DisplayName("GET /v3/api-docs returns SmartREST API title, author Alessio Saltarin, and ISC license")
    void testOpenApiMetadata() throws Exception
    {
        mockMvc.perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title", is("SmartREST API")))
                .andExpect(jsonPath("$.info.contact.name", is("Alessio Saltarin")))
                .andExpect(jsonPath("$.info.license.name", is("ISC")))
                .andExpect(jsonPath("$.info.license.url", is("https://opensource.org/licenses/ISC")));
    }

    @Test
    @DisplayName("GET /swagger-ui/index.html returns 200 OK")
    void testSwaggerUiIndex() throws Exception
    {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @DisplayName("GET /swagger-ui/swagger-initializer.js contains light theme script, document.title, and idea syntax highlight")
    void testSwaggerInitializerTransformation() throws Exception
    {
        mockMvc.perform(get("/swagger-ui/swagger-initializer.js"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("smartrest-light-theme")))
                .andExpect(content().string(containsString("document.title = 'SmartREST API'")))
                .andExpect(content().string(containsString("theme: \"idea\"")))
                .andExpect(content().string(containsString("classList.remove('dark-mode')")));
    }
}
