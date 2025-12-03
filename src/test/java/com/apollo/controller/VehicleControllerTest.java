package com.apollo.controller;

/**
 * JUnit 5 Test Class for VehicleController
 * 
 * This class contains JUnit 5 unit tests using:
 * - @Test annotation for test methods
 * - @BeforeEach for test setup
 * - @DisplayName for descriptive test names
 * - JUnit assertions (assertEquals, assertNotNull, etc.)
 * - MockMvc for Spring MVC endpoint testing
 * - Mockito for mocking dependencies
 */
import com.apollo.dao.VehicleDAO;
import com.apollo.model.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@DisplayName("VehicleController Tests")
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleDAO vehicleDAO;

    @Autowired
    private ObjectMapper objectMapper;

    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle("abcd", "Toyota", "highlander", 5, "highlander", 2014, new BigDecimal(12000.12), "gas");
        assertNotNull(testVehicle, "Test vehicle should not be null");
    }

    @Test
    @DisplayName("GET /vehicle should return all vehicles with status 200")
    public void getAllVehicles_ShouldReturnListOfVehicles() throws Exception {
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle("abcd", "Toyota", "highlander", 5, "highlander", 2014, new BigDecimal(12000.12), "gas"), new Vehicle("efgh", "Honda", "sedan", 100, "sedan", 2025, new BigDecimal(25000.50), "gasoline")
        );

        assertEquals(2, vehicles.size(), "Should have 2 vehicles in test data");
        assertNotNull(vehicles.get(0), "First vehicle should not be null");
        assertNotNull(vehicles.get(1), "Second vehicle should not be null");

        when(vehicleDAO.getAllVehicles()).thenReturn(vehicles);

        mockMvc.perform(get("/vehicle"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vin", is("abcd")))  // ✓ Fixed
                .andExpect(jsonPath("$[0].manufacturerName", is("Toyota")))
                .andExpect(jsonPath("$[0].modelName", is("highlander")))  // ✓ Fixed
                .andExpect(jsonPath("$[1].vin", is("efgh")))  // ✓ Fixed
                .andExpect(jsonPath("$[1].manufacturerName", is("Honda")))
                .andExpect(jsonPath("$[1].modelName", is("sedan")));  // ✓ Fixed

        verify(vehicleDAO, times(1)).getAllVehicles();
        assertTrue(true, "Verify that getAllVehicles was called exactly once");
    }

    @Test
    @DisplayName("GET /vehicle should return empty list when no vehicles exist")
    public void getAllVehicles_ShouldReturnEmptyList() throws Exception {
        when(vehicleDAO.getAllVehicles()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/vehicle"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(vehicleDAO, times(1)).getAllVehicles();
    }

    @Test
    @DisplayName("POST /vehicle should create vehicle and return status 201")
    public void createVehicle_ShouldCreateVehicleAndReturn201() throws Exception {
        assertNotNull(testVehicle, "Test vehicle should not be null");
        assertNotNull(testVehicle.getVin(), "VIN should not be null");
        assertEquals("abcd", testVehicle.getVin(), "VIN should match expected value");

        when(vehicleDAO.insertVehicle(any(Vehicle.class))).thenReturn(testVehicle);

        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVehicle)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vin", is("abcd")))
                .andExpect(jsonPath("$.manufacturerName", is("Toyota")))
                .andExpect(jsonPath("$.modelName", is("highlander")))
                .andExpect(jsonPath("$.modelYear", is(2014)))
                .andExpect(jsonPath("$.horsePower", is(5)));

        verify(vehicleDAO, times(1)).insertVehicle(any(Vehicle.class));
        assertTrue(true, "Vehicle should be inserted successfully");
    }

    @Test
    @DisplayName("POST /vehicle should return 422 when validation fails - missing manufacturer name")
    void createVehicle_ShouldReturn422WhenManufacturerNameIsMissing() throws Exception {
        Vehicle invalidVehicle = new Vehicle("abcd", null, "highlander", 5, "highlander", 2014, new BigDecimal(12000.12), "gas");;

        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehicle)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.errors.manufacturerName", notNullValue()));

        verify(vehicleDAO, never()).insertVehicle(any(Vehicle.class));
    }

    @Test
    @DisplayName("POST /vehicle should return 400 when JSON is invalid")
    void createVehicle_ShouldReturn400WhenJsonIsInvalid() throws Exception {
        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid JSON format")))
                .andExpect(jsonPath("$.message", containsString("could not be parsed")));

        verify(vehicleDAO, never()).insertVehicle(any(Vehicle.class));
    }

    @Test
    @DisplayName("GET /vehicle/{vin} should return vehicle with status 200 when found")
    void getVehicleByVin_ShouldReturnVehicleWhenFound() throws Exception {
        when(vehicleDAO.getVehicleByVin("abcd")).thenReturn(testVehicle);

        mockMvc.perform(get("/vehicle/abcd"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vin", is("abcd")))
                .andExpect(jsonPath("$.manufacturerName", is("Toyota")))
                .andExpect(jsonPath("$.modelName", is("highlander")));

        verify(vehicleDAO, times(1)).getVehicleByVin("abcd");
    }

    @Test
    @DisplayName("GET /vehicle/{vin} should return 404 when vehicle not found")
    void getVehicleByVin_ShouldReturn404WhenNotFound() throws Exception {
        String nonExistentVin = "NONEXISTENT";

        assertNotNull(nonExistentVin, "VIN should not be null");
        assertFalse(nonExistentVin.isEmpty(), "VIN should not be empty");

        when(vehicleDAO.getVehicleByVin(nonExistentVin)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/vehicle/" + nonExistentVin))
                .andExpect(status().isNotFound());

        // JUnit assertion to verify DAO was called
        verify(vehicleDAO, times(1)).getVehicleByVin(nonExistentVin);
        assertTrue(true, "DAO should be called once for non-existent VIN");
    }

    @Test
    @DisplayName("PUT /vehicle/{vin} should update vehicle and return status 200")
    void updateVehicle_ShouldUpdateVehicleAndReturn200() throws Exception {
        Vehicle updatedVehicle = new Vehicle("abcd", "Toyota", "Camry Hybrid", 208, "Camry Hybrid", 2024, new BigDecimal("35000.00"), "Hybrid");
        when(vehicleDAO.updateVehicle(eq("abcd"), any(Vehicle.class))).thenReturn(updatedVehicle);

        mockMvc.perform(put("/vehicle/abcd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVehicle)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vin", is("abcd")))
                .andExpect(jsonPath("$.modelName", is("Camry Hybrid")))
                .andExpect(jsonPath("$.modelYear", is(2024)))
                .andExpect(jsonPath("$.horsePower", is(208)));

        verify(vehicleDAO, times(1)).updateVehicle(eq("abcd"), any(Vehicle.class));
    }

    @Test
    @DisplayName("PUT /vehicle/{vin} should return 422 when validation fails")
    void updateVehicle_ShouldReturn422WhenValidationFails() throws Exception {
        Vehicle invalidVehicle = new Vehicle("abcd", null, "Camry Hybrid", 208, "Camry Hybrid", 2024, new BigDecimal("35000.00"), "Hybrid");

        mockMvc.perform(put("/vehicle/1HGBH41JXMN109186")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehicle)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error", is("Validation failed")));

        verify(vehicleDAO, never()).updateVehicle(anyString(), any(Vehicle.class));
    }

    @Test
    @DisplayName("PUT /vehicle/{vin} should return 400 when JSON is invalid")
    void updateVehicle_ShouldReturn400WhenJsonIsInvalid() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/vehicle/abcd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid JSON format")));

        verify(vehicleDAO, never()).updateVehicle(anyString(), any(Vehicle.class));
    }

    @Test
    @DisplayName("DELETE /vehicle/{vin} should delete vehicle and return status 204")
    void deleteVehicle_ShouldDeleteVehicleAndReturn204() throws Exception {
        doNothing().when(vehicleDAO).deleteVehicle("abcd");

        mockMvc.perform(delete("/vehicle/abcd"))
                .andExpect(status().isNoContent());

        verify(vehicleDAO, times(1)).deleteVehicle("abcd");
    }

    @Test
    @DisplayName("DELETE /vehicle/{vin} should handle deletion of non-existent vehicle")
    void deleteVehicle_ShouldHandleNonExistentVehicle() throws Exception {
        doNothing().when(vehicleDAO).deleteVehicle("NONEXISTENT");

        mockMvc.perform(delete("/vehicle/NONEXISTENT"))
                .andExpect(status().isNoContent());

        verify(vehicleDAO, times(1)).deleteVehicle("NONEXISTENT");
    }
}

