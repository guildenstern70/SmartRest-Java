/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.littlelite.smartrest.dao.BrandRepository;
import net.littlelite.smartrest.dao.CarRepository;
import net.littlelite.smartrest.dao.DealerRepository;
import net.littlelite.smartrest.dto.CreateBrandDto;
import net.littlelite.smartrest.dto.CreateCarDto;
import net.littlelite.smartrest.dto.CreateDealerDto;
import net.littlelite.smartrest.model.Brand;
import net.littlelite.smartrest.model.Dealer;
import net.littlelite.smartrest.model.FuelType;
import net.littlelite.smartrest.model.TransmissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class CrudApiControllerTests
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private CarRepository carRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // --- Brand Tests ---

    @Test
    @DisplayName("POST /api/brands creates brand with valid payload")
    void testCreateBrandSuccess() throws Exception
    {
        CreateBrandDto brandDto = new CreateBrandDto("Ferrari", "Italy");

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Ferrari")))
                .andExpect(jsonPath("$.country", is("Italy")));
    }

    @Test
    @DisplayName("POST /api/brands rejects empty or blank fields")
    void testCreateBrandValidationBlankFields() throws Exception
    {
        CreateBrandDto invalidDto = new CreateBrandDto("", "   ");

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.country", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/brands rejects duplicate brand name")
    void testCreateBrandDuplicateName() throws Exception
    {
        CreateBrandDto duplicateDto = new CreateBrandDto("bmw", "Germany");

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    @DisplayName("DELETE /api/brands/{id} deletes unlinked brand")
    void testDeleteBrandSuccess() throws Exception
    {
        Brand brand = brandRepository.save(Brand.builder().name("Alfa Romeo").country("Italy").build());

        mockMvc.perform(delete("/api/brands/" + brand.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/brands/" + brand.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/brands/{id} returns 404 for nonexistent brand")
    void testDeleteBrandNotFound() throws Exception
    {
        mockMvc.perform(delete("/api/brands/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/brands/{id} rejects deletion of brand associated with dealer")
    void testDeleteBrandConflictWithDealer() throws Exception
    {
        Brand bmw = brandRepository.findByNameIgnoreCase("BMW").orElseThrow();

        mockMvc.perform(delete("/api/brands/" + bmw.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("associated with dealer")));
    }

    // --- Dealer Tests ---

    @Test
    @DisplayName("POST /api/dealers creates dealer with 1 or 2 brands")
    void testCreateDealerSuccess() throws Exception
    {
        Brand bmw = brandRepository.findByNameIgnoreCase("BMW").orElseThrow();
        Brand tesla = brandRepository.findByNameIgnoreCase("Tesla").orElseThrow();

        CreateDealerDto dealerDto = new CreateDealerDto(
                "Modern Auto Hub",
                "Florence",
                Set.of(bmw.getId(), tesla.getId())
        );

        mockMvc.perform(post("/api/dealers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealerDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Modern Auto Hub")))
                .andExpect(jsonPath("$.city", is("Florence")))
                .andExpect(jsonPath("$.brands", hasSize(2)));
    }

    @Test
    @DisplayName("POST /api/dealers rejects dealer with 0 or >2 brands")
    void testCreateDealerInvalidBrandCount() throws Exception
    {
        CreateDealerDto zeroBrands = new CreateDealerDto("Empty Dealer", "Naples", Set.of());

        mockMvc.perform(post("/api/dealers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zeroBrands)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.brandIds", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/dealers rejects nonexistent brand ID")
    void testCreateDealerNonExistentBrand() throws Exception
    {
        CreateDealerDto dealerDto = new CreateDealerDto("Ghost Dealer", "Turin", Set.of(99999L));

        mockMvc.perform(post("/api/dealers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Brand with ID 99999 not found")));
    }

    @Test
    @DisplayName("DELETE /api/dealers/{id} deletes dealer successfully")
    void testDeleteDealerSuccess() throws Exception
    {
        Brand toyota = brandRepository.findByNameIgnoreCase("Toyota").orElseThrow();
        Dealer dealer = dealerRepository.save(Dealer.builder()
                .name("Temp Dealership")
                .city("Genoa")
                .brands(Set.of(toyota))
                .build());

        mockMvc.perform(delete("/api/dealers/" + dealer.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/dealers/" + dealer.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/dealers/{id} returns 404 for nonexistent dealer")
    void testDeleteDealerNotFound() throws Exception
    {
        mockMvc.perform(delete("/api/dealers/99999"))
                .andExpect(status().isNotFound());
    }

    // --- Car Tests ---

    @Test
    @DisplayName("POST /api/cars creates car without dealer")
    void testCreateCarWithoutDealerSuccess() throws Exception
    {
        Brand porsche = brandRepository.findByNameIgnoreCase("Porsche").orElseThrow();

        CreateCarDto carDto = new CreateCarDto(
                "Cayman GTS",
                porsche.getId(),
                400,
                4.0,
                2,
                new BigDecimal("92000.00"),
                FuelType.PETROL,
                TransmissionType.MANUAL,
                null
        );

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Cayman GTS")))
                .andExpect(jsonPath("$.brandId", is(porsche.getId().intValue())))
                .andExpect(jsonPath("$.dealerId", nullValue()));
    }

    @Test
    @DisplayName("POST /api/cars creates car with dealer carrying the brand")
    void testCreateCarWithDealerSuccess() throws Exception
    {
        Brand bmw = brandRepository.findByNameIgnoreCase("BMW").orElseThrow();
        Dealer bavariaMotors = dealerRepository.findAll().stream()
                .filter(d -> d.getName().equals("Bavaria Motors"))
                .findFirst()
                .orElseThrow();

        CreateCarDto carDto = new CreateCarDto(
                "M4 Competition",
                bmw.getId(),
                510,
                3.5,
                4,
                new BigDecimal("105000.00"),
                FuelType.PETROL,
                TransmissionType.AUTOMATIC,
                bavariaMotors.getId()
        );

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("M4 Competition")))
                .andExpect(jsonPath("$.dealerId", is(bavariaMotors.getId().intValue())));
    }

    @Test
    @DisplayName("POST /api/cars rejects car when dealer does not carry brand")
    void testCreateCarDealerMismatchedBrand() throws Exception
    {
        Brand tesla = brandRepository.findByNameIgnoreCase("Tesla").orElseThrow();
        Dealer bavariaMotors = dealerRepository.findAll().stream()
                .filter(d -> d.getName().equals("Bavaria Motors"))
                .findFirst()
                .orElseThrow();

        CreateCarDto carDto = new CreateCarDto(
                "Model 3",
                tesla.getId(),
                300,
                5.0,
                5,
                new BigDecimal("45000.00"),
                FuelType.ELECTRIC,
                TransmissionType.AUTOMATIC,
                bavariaMotors.getId()
        );

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("does not carry brand")));
    }

    @Test
    @DisplayName("POST /api/cars validates positive power, passengers, and price")
    void testCreateCarValidationInvalidAttributes() throws Exception
    {
        Brand bmw = brandRepository.findByNameIgnoreCase("BMW").orElseThrow();

        CreateCarDto invalidDto = new CreateCarDto(
                "",
                bmw.getId(),
                -10,
                -1.0,
                0,
                new BigDecimal("-50.00"),
                FuelType.PETROL,
                TransmissionType.MANUAL,
                null
        );

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.power", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.zeroToHundredKmh", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.passengers", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.price", notNullValue()));
    }

    @Test
    @DisplayName("DELETE /api/cars/{id} deletes car successfully")
    void testDeleteCarSuccess() throws Exception
    {
        Brand bmw = brandRepository.findByNameIgnoreCase("BMW").orElseThrow();
        net.littlelite.smartrest.model.Car car = carRepository.save(net.littlelite.smartrest.model.Car.builder()
                .name("Car To Delete")
                .brand(bmw)
                .power(200)
                .zeroToHundredKmh(7.0)
                .passengers(5)
                .price(new BigDecimal("30000.00"))
                .type(FuelType.PETROL)
                .transmission(TransmissionType.MANUAL)
                .build());

        mockMvc.perform(delete("/api/cars/" + car.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cars/" + car.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/cars/{id} returns 404 for nonexistent car")
    void testDeleteCarNotFound() throws Exception
    {
        mockMvc.perform(delete("/api/cars/99999"))
                .andExpect(status().isNotFound());
    }
}
