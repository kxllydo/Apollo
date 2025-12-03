package com.apollo.dao;

import com.apollo.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VehicleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Vehicle> vehicleRowMapper = new RowMapper<Vehicle>() {
        @Override
        public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vehicle vehicle = new Vehicle();
            vehicle.setVin(rs.getString("vin"));
            vehicle.setManufacturerName(rs.getString("manufacturer_name"));
            vehicle.setDescription(rs.getString("description"));
            vehicle.setHorsePower(rs.getObject("horse_power", Integer.class));
            vehicle.setModelName(rs.getString("model_name"));
            vehicle.setModelYear(rs.getObject("model_year", Integer.class));
            vehicle.setPurchasePrice(rs.getBigDecimal("purchase_price"));
            vehicle.setFuelType(rs.getString("fuel_type"));
            return vehicle;
        }
    };

    public List<Vehicle> getAllVehicles() {
        String sql = "SELECT * FROM vehicle";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    public Vehicle getVehicleByVin(String vin) {
        String sql = "SELECT * FROM vehicle WHERE vin = ?";
        List<Vehicle> vehicles = jdbcTemplate.query(sql, vehicleRowMapper, vin);
        if (vehicles.isEmpty()){
            return null;
        } else {
            return vehicles.get(0);
        }
    }

    public Vehicle insertVehicle(Vehicle vehicle) {
        Vehicle existingVehicle = getVehicleByVin(vehicle.getVin());
        if (existingVehicle != null) {
            return null;
        }
        String sql = "INSERT INTO vehicle (vin, manufacturer_name, description, horse_power, model_name, model_year, purchase_price, fuel_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                vehicle.getVin(),
                vehicle.getManufacturerName(),
                vehicle.getDescription(),
                vehicle.getHorsePower(),
                vehicle.getModelName(),
                vehicle.getModelYear(),
                vehicle.getPurchasePrice(),
                vehicle.getFuelType());
        return getVehicleByVin(vehicle.getVin());
    }

    public Vehicle updateVehicle(String vin, Vehicle vehicle) {
        Vehicle existingVehicle = getVehicleByVin(vehicle.getVin());
        if (existingVehicle != null) {
            return null;
        }

        String sql = "UPDATE vehicle SET manufacturer_name = ?, description = ?, horse_power = ?, model_name = ?, " +
                     "model_year = ?, purchase_price = ?, fuel_type = ? WHERE vin = ?";
        jdbcTemplate.update(sql,
                vehicle.getManufacturerName(),
                vehicle.getDescription(),
                vehicle.getHorsePower(),
                vehicle.getModelName(),
                vehicle.getModelYear(),
                vehicle.getPurchasePrice(),
                vehicle.getFuelType(),
                vin);
        return getVehicleByVin(vin);
    }

    public void deleteVehicle(String vin) {
        String sql = "DELETE FROM vehicle WHERE vin = ?";
        jdbcTemplate.update(sql, vin);
    }

}
