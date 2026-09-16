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
class ApiControllerTests
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private net.littlelite.smartrest.service.AliveService aliveService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("GET /api/brands returns 5 brands")
    void testGetBrands() throws Exception
    {
        mockMvc.perform(get("/api/brands").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].name", hasItems("BMW", "Audi", "Tesla", "Toyota", "Porsche")));
    }

    @Test
    @DisplayName("GET /api/dealers returns 5 dealers with brands and available cars count")
    void testGetDealers() throws Exception
    {
        mockMvc.perform(get("/api/dealers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].brands", not(empty())))
                .andExpect(jsonPath("$[0].availableCarsCount", greaterThan(0)));
    }

    @Test
    @DisplayName("GET /api/cars returns 20 cars with detailed attributes")
    void testGetCars() throws Exception
    {
        mockMvc.perform(get("/api/cars").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].power", greaterThan(0)))
                .andExpect(jsonPath("$[0].zeroToHundredKmh", greaterThan(0.0)))
                .andExpect(jsonPath("$[0].type", notNullValue()))
                .andExpect(jsonPath("$[0].transmission", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/cars?fuelType=ELECTRIC filters only electric cars")
    void testGetCarsFilteredByFuelType() throws Exception
    {
        mockMvc.perform(get("/api/cars").param("fuelType", "ELECTRIC").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].type", everyItem(is("ELECTRIC"))));
    }

    @Test
    @DisplayName("GET /api/alive returns alive=true, application version, and sanitized database URL")
    void testGetAlive() throws Exception
    {
        mockMvc.perform(get("/api/alive").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alive", is(true)))
                .andExpect(jsonPath("$.version", is(aliveService.getVersion())))
                .andExpect(jsonPath("$.database", is("jdbc:h2:mem:smartrestdb")));
    }
}
