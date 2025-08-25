package main.repository;

import main.config.AppConstants;
import main.config.DatabaseConfig;
import main.model.Vehicle;
import main.utils.VehicleLinkedList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VehicleRepository extends Repository {
    private final String INSERT_VEHICLE_SQL = "Insert into vehicle values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String SELECT_VEHICLE_BY_PLATE_SQL = "Select * From vehicle where license_plate=?";
    private final String SELECT_VEHICLE_BY_ENGINE_NUMBER_SQL = "Select * From vehicle where engine_number=?";
    private final String SELECT_VEHICLE_BY_CHASIS_NUMBER_SQL = "Select * From vehicle where chasis_number=?";
    private final String SELECT_VEHICLES_BY_USER_ID_SQL = "Select * From vehicle where user_id=?";
    private final String UPDATE_INSURANCE_EXPIRY_SQL = "Update vehicle set insurance_expiry_date=? where license_plate=?";
    private final String UPDATE_PUC_EXPIRY_SQL = "Update vehicle set puc_expiry_date=? where license_plate=?";
    private final String UPDATE_RC_EXPIRY_SQL = "Update vehicle set status=?, rc_expiry_date=? where license_plate=?";
    private final String UPDATE_VEHICLE_STATUS_SQL = "Update vehicle set status=? where license_plate=?";
    private final String COUNT_TOTAL_VEHICLES = "Select COUNT(*) From vehicle";
    private final String SELECT_VEHICLE_CATEGORY_BY_PLATE_SQL = "Select CONCAT_WS('-', VC.category_name, VC.description) from vehicle_category VC join vehicle V on VC.category_id=V.category_id where V.license_plate=?";
    private final String SELECT_ALL_VEHICLE_CATEGORY_SQL = "Select category_id, CONCAT_WS('-', category_name, description) from vehicle_category";

    public void save(Object obj) {
        try {
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(INSERT_VEHICLE_SQL);
            Vehicle vehicle = (Vehicle) obj;
            pst.setString(1, vehicle.getLicense_plate());
            pst.setString(2, vehicle.getLicense_number());
            pst.setInt(3, vehicle.getCategory_id());
            pst.setString(4, vehicle.getEngine_number());
            pst.setString(5, vehicle.getChassis_number());
            pst.setDate(6, Date.valueOf(vehicle.getInsurance_expiry_date()));
            pst.setDate(7, Date.valueOf(vehicle.getPuc_expiry_date()));
            pst.setString(8, vehicle.getStatus());
            pst.setString(9, vehicle.getVname());
            pst.setInt(10, vehicle.getUser_id());
            pst.setString(11, vehicle.getState());
            pst.setDate(12, Date.valueOf(vehicle.getRc_expiry_date()));
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countTotalVehicles() throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(COUNT_TOTAL_VEHICLES);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public HashMap<Integer, String> getAllCategory() throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_ALL_VEHICLE_CATEGORY_SQL);
        HashMap<Integer, String> categories = new HashMap<>();
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            categories.put(rs.getInt(1), rs.getString(2));
        }
        return categories;
    }

    public String getVehicleCategory(String licensePlate) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_VEHICLE_CATEGORY_BY_PLATE_SQL);
        pst.setString(1, licensePlate);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public Vehicle findByLicensePlate(String licensePlate) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_VEHICLE_BY_PLATE_SQL);
        pst.setString(1, licensePlate);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new Vehicle(rs.getString("license_plate"), rs.getString("license_number"), rs.getInt("category_id"), rs.getString("engine_number"), rs.getString("chasis_number"), rs.getDate("insurance_expiry_date").toLocalDate(), rs.getDate("puc_expiry_date").toLocalDate(), rs.getString("status"), rs.getString("vname"), rs.getInt("user_id"), rs.getString("state"), rs.getDate("rc_expiry_date").toLocalDate());
        }
        return null;
    }

    public Vehicle findByEngineNumber(String engineNumber) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_VEHICLE_BY_ENGINE_NUMBER_SQL);
        pst.setString(1, engineNumber);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new Vehicle(rs.getString("license_plate"), rs.getString("license_number"), rs.getInt("category_id"), rs.getString("engine_number"), rs.getString("chasis_number"), rs.getDate("insurance_expiry_date").toLocalDate(), rs.getDate("puc_expiry_date").toLocalDate(), rs.getString("status"), rs.getString("vname"), rs.getInt("user_id"), rs.getString("state"), rs.getDate("rc_expiry_date").toLocalDate());
        }
        return null;
    }

    public Vehicle findByChasisNumber(String chasisNumber) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_VEHICLE_BY_CHASIS_NUMBER_SQL);
        pst.setString(1, chasisNumber);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new Vehicle(rs.getString("license_plate"), rs.getString("license_number"), rs.getInt("category_id"), rs.getString("engine_number"), rs.getString("chasis_number"), rs.getDate("insurance_expiry_date").toLocalDate(), rs.getDate("puc_expiry_date").toLocalDate(), rs.getString("status"), rs.getString("vname"), rs.getInt("user_id"), rs.getString("state"), rs.getDate("rc_expiry_date").toLocalDate());
        }
        return null;
    }

    public VehicleLinkedList findByUserId(int user_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_VEHICLES_BY_USER_ID_SQL);
        pst.setInt(1, user_id);
        ResultSet rs = pst.executeQuery();
        VehicleLinkedList vehicles = new VehicleLinkedList();
        while (rs.next()) {
            vehicles.insertLast(new Vehicle(rs.getString("license_plate"), rs.getString("license_number"), rs.getInt("category_id"), rs.getString("engine_number"), rs.getString("chasis_number"), rs.getDate("insurance_expiry_date").toLocalDate(), rs.getDate("puc_expiry_date").toLocalDate(), rs.getString("status"), rs.getString("vname"), rs.getInt("user_id"), rs.getString("state"), rs.getDate("rc_expiry_date").toLocalDate()));
        }
        return vehicles;
    }

    public void updateInsuranceExpiry(String licensePlate, LocalDate newExpiryDate) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_INSURANCE_EXPIRY_SQL);
        pst.setDate(1, Date.valueOf(newExpiryDate));
        pst.setString(2, licensePlate);
        pst.executeUpdate();
    }

    public void updatePUCExpiry(String licensePlate, LocalDate newExpiryDate) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_PUC_EXPIRY_SQL);
        pst.setDate(1, Date.valueOf(newExpiryDate));
        pst.setString(2, licensePlate);
        pst.executeUpdate();
    }

    public void updateRCExpiry(String licensePlate, LocalDate newExpiryDate) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_RC_EXPIRY_SQL);
        pst.setString(1, AppConstants.VEHICLE_ACTIVE);
        pst.setDate(2, Date.valueOf(newExpiryDate));
        pst.setString(3, licensePlate);
        pst.executeUpdate();
    }

    public void updateStatus(String licensePlate, String status) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_VEHICLE_STATUS_SQL);
        pst.setString(1, status);
        pst.setString(2, licensePlate);
        pst.executeUpdate();
    }
}
