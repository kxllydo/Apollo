package com.apollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @NotBlank(message = "VIN is required")
    @Size(max = 17, message = "VIN must be at most 17 characters")
    @Column(name = "vin", length = 17, nullable = false, unique = true)
    private String vin;

    @NotBlank(message = "Manufacturer name is required")
    @Size(max = 255, message = "Manufacturer name must be at most 255 characters")
    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @NotBlank(message = "Description is required")
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotNull(message = "Horse power is required")
    @Min(value = 1, message = "Horse power must be at least 1")
    @Column(name = "horse_power", nullable = false)
    private Integer horsePower;

    @NotBlank(message = "Model name is required")
    @Size(max = 255, message = "Model name must be at most 255 characters")
    @Column(name = "model_name", nullable = false)
    private String modelName;

    @NotNull(message = "Model year is required")
    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @NotNull(message = "Purchase price is required")
    @Column(name = "purchase_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal purchasePrice;

    @NotBlank(message = "Fuel type is required")
    @Size(max = 50, message = "Fuel type must be at most 50 characters")
    @Column(name = "fuel_type", length = 50, nullable = false)
    private String fuelType;

    public Vehicle(){

    }
    public Vehicle(String vin, String manufacturerName, String description, Integer horsePower, String modelName, Integer modelYear, BigDecimal purchasePrice, String fuelType) {
        this.vin = vin;
        this.manufacturerName = manufacturerName;
        this.description = description;
        this.horsePower = horsePower;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.purchasePrice = purchasePrice;
        this.fuelType = fuelType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vin='" + vin + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", description='" + description + '\'' +
                ", horsePower=" + horsePower +
                ", modelName='" + modelName + '\'' +
                ", modelYear=" + modelYear +
                ", purchasePrice=" + purchasePrice +
                ", fuelType='" + fuelType + '\'' +
                '}';
    }
}

