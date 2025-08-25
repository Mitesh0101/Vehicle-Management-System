package main.repository;

import main.config.DatabaseConfig;
import main.model.DrivingLicense;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DrivingLicenseRepository extends Repository {
    private final String INSERT_LICENSE_SQL = "Insert into driving_license values(?,?,?,?,?);";
    private final String SELECT_LICENSE_BY_USER_ID_SQL = "Select * From driving_license where user_id=?;";
    private final String SELECT_LICENSE_BY_NUMBER_SQL = "Select * From driving_license where license_number=?;";
    private final String UPDATE_LICENSE_EXPIRY_SQL = "Update driving_license set expiry_date=? where license_number=?";
    private final String UPDATE_LICENSE_STATUS_SQL = "Update driving_license set status=? where license_number=?";
    private final String COUNT_TOTAL_LICENSES = "Select COUNT(*) From driving_license;";
    private final String SELECT_CATEGORY_BY_NUMBER_SQL = "Select CONCAT_WS('-', VC.category_name, VC.description) from driving_license DL join driving_license_category DLC on DL.license_number = DLC.license_number join vehicle_category VC on DLC.category_id=VC.category_id where DL.license_number=?;";
    private final String ISSUE_LICENSE_PROCEDURE_SQL = "CALL issue_license(?, ?, ?)";

    public void save(Object obj) {
        try {
            DrivingLicense license = (DrivingLicense) obj;
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(INSERT_LICENSE_SQL);
            pst.setString(1, license.getLicense_number());
            pst.setInt(2, license.getUser_id());
            pst.setDate(3, Date.valueOf(license.getIssue_date()));
            pst.setDate(4, Date.valueOf(license.getExpiry_date()));
            pst.setString(5, license.getStatus());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("License could not be inserted successfully!");
        }
    }

    public void issueLicense(String license_number, int user_id, int category_id) throws SQLException {
        CallableStatement cst = DatabaseConfig.getConnection().prepareCall(ISSUE_LICENSE_PROCEDURE_SQL);
        cst.setString(1, license_number);
        cst.setInt(2, user_id);
        cst.setInt(3, category_id);
        cst.execute();
    }

    public void updateLicenseStatus(String licenseNumber, String status) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_LICENSE_STATUS_SQL);
        pst.setString(1, status);
        pst.setString(2, licenseNumber);
        pst.executeUpdate();
    }

    public int getTotalLicensesCount() throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(COUNT_TOTAL_LICENSES);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public DrivingLicense findByUserId(int user_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_LICENSE_BY_USER_ID_SQL);
        pst.setInt(1, user_id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new DrivingLicense(rs.getString("license_number"), rs.getInt("user_id"), rs.getDate("issue_date").toLocalDate(), rs.getDate("expiry_date").toLocalDate(), rs.getString("status"));
        }
        return null;
    }

    public DrivingLicense findByNumber(String licenseNumber) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_LICENSE_BY_NUMBER_SQL);
        pst.setString(1, licenseNumber);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new DrivingLicense(rs.getString("license_number"), rs.getInt("user_id"), rs.getDate("issue_date").toLocalDate(), rs.getDate("expiry_date").toLocalDate(), rs.getString("status"));
        }
        return null;
    }

    public List<String> findCategoryByNumber(String licenseNumber) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_CATEGORY_BY_NUMBER_SQL);
        pst.setString(1, licenseNumber);
        List<String> categories = new ArrayList<>();
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            categories.add(rs.getString(1));
        }
        return categories;
    }

    public void updateExpiry(String licenseNumber, LocalDate newExpiryDate) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_LICENSE_EXPIRY_SQL);
        pst.setDate(1, Date.valueOf(newExpiryDate));
        pst.setString(2, licenseNumber);
        pst.executeUpdate();
    }
}
