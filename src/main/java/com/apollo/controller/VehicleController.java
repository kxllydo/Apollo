package com.apollo.controller;

import com.apollo.model.Vehicle;
import com.apollo.dao.VehicleDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleDAO vehicleDAO;

    @Autowired
    public VehicleController(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleDAO.insertVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }

    @GetMapping("/{vin}")
    public ResponseEntity<?> getVehicleByVin(@PathVariable String vin) {
        Vehicle vehicle = vehicleDAO.getVehicleByVin(vin);
        if (vehicle == null) {
            return ResponseEntity.status(422).body("Vin not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(vehicle);
        }
    }

    @PutMapping("/{vin}")
    public ResponseEntity<?> updateVehicle(@PathVariable String vin, @Valid @RequestBody Vehicle vehicle) {
        Vehicle updatedVehicle = vehicleDAO.updateVehicle(vin, vehicle);
        if (updatedVehicle == null) {
            return ResponseEntity.status(422).body("Vin not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedVehicle);
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String vin) {
        vehicleDAO.deleteVehicle(vin);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid JSON format");
        error.put("message", "The request body could not be parsed as a Vehicle JSON representation.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation failed");
        
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    error -> error.getField(),
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                ));
        
        errorResponse.put("errors", errors);
        return ResponseEntity.status(422).body(errorResponse);
    }

    
}

