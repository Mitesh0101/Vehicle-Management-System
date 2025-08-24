package main.repository;

import main.config.AppConstants;
import main.config.DatabaseConfig;
import main.model.DrivingLicense;
import main.model.Payment;
import main.model.Vehicle;
import main.utils.SessionUtils;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository extends Repository {
    private final String INSERT_PAYMENT_SQL = "Insert into payments(payment_for, ref_id, amount, payment_date, mode, user_id) values (?, ?, ?, ?, ?, ?);";
    private final String SELECT_TOTAL_PAYMENT_BY_ID = "Select SUM(amount) From payments where user_id=?";
    private final String SELECT_BY_CHALLAN_ID="Select * from e_challan join vehicle on e_challan.license_plate=vehicle.license_plate where user_id=? and challan_id=?";
    private final String SELECT_BY_EXAM_ID="Select * from driving_exam where user_id=? and exam_id=?";
    private final String COUNT_TOTAL_PAYMENT_AMOUNT_SQL = "Select SUM(amount) from payments;";


    public void save(Object obj) {
        Payment payment = (Payment) obj;
        try {
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(INSERT_PAYMENT_SQL);
            pst.setString(1, payment.getPayment_for());
            pst.setString(2, payment.getRef_id());
            pst.setDouble(3, payment.getAmount());
            pst.setDate(4, Date.valueOf(payment.getPayment_date()));
            pst.setString(5, payment.getMode());
            pst.setInt(6, SessionUtils.getCurrentUser().getUser_id());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Payment not inserted into database!");
        }
    }

    public double getTotalPaymentsAmount() throws SQLException {
        PreparedStatement pst=DatabaseConfig.getConnection().prepareStatement(COUNT_TOTAL_PAYMENT_AMOUNT_SQL);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getDouble(1);
        }
        return -1;
    }

    public double findTotalAmountOfChallan(int user_id) throws SQLException{
        PreparedStatement pst=DatabaseConfig.getConnection().prepareStatement(SELECT_TOTAL_PAYMENT_BY_ID);
        pst.setInt(1,user_id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getDouble(1);
        }
        return -1;
    }

    public boolean checkReferenceId(String reason, String refid) throws SQLException {
        PreparedStatement pst;

        // PAYMENT_REASON[0] = "Challan"
        if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[0])) {
            pst = DatabaseConfig.getConnection().prepareStatement(SELECT_BY_CHALLAN_ID);
        }
        // PAYMENT_REASON[1] = "Driving Exam"
        else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[1])){
            pst = DatabaseConfig.getConnection().prepareStatement(SELECT_BY_EXAM_ID);
        }
        else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[5])) {
            DrivingLicense license = AppConstants.drivingLicenseRepository.findByNumber(refid);
            return license != null;
        }
        // All other payment reasons are vehicle related so everything incorporated in else
        else {
            Vehicle vehicle = AppConstants.vehicleRepository.findByLicensePlate(refid);
            return vehicle != null;
        }

        int refId = Integer.parseInt(refid);
        pst.setInt(1, SessionUtils.getCurrentUser().getUser_id());
        pst.setInt(2, refId);
        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    public boolean checkStatusOfChallan(String reason, String refid) throws SQLException {
        if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[0])) {
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_BY_CHALLAN_ID);
            pst.setInt(1, SessionUtils.getCurrentUser().getUser_id());
            pst.setInt(2, Integer.parseInt(refid));

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String stats = rs.getString("status");
                if (stats.equals(AppConstants.CHALLAN_PENDING))
                    return true;
                else
                    return false;
            }
            return false;
        } else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[1])){
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_BY_EXAM_ID);
            pst.setInt(1, SessionUtils.getCurrentUser().getUser_id());
            pst.setInt(2, Integer.parseInt(refid));

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String stats = rs.getString("payment_status");
                if (stats.equals(AppConstants.PAYMENT_UNPAID))
                    return true;
                else
                    return false;
            }
            return false;
        } else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[5])) {
            DrivingLicense license = AppConstants.drivingLicenseRepository.findByNumber(refid);
            return license.getStatus().equalsIgnoreCase(AppConstants.LICENSE_EXPIRED);
        }
        // handles all other cases in else block as all other reasons are vehicle related
        else {
            Vehicle vehicle = AppConstants.vehicleRepository.findByLicensePlate(refid);
            if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[2])) {
                return vehicle.getPuc_expiry_date().isBefore(LocalDate.now());
            }
            else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[3])) {
                return vehicle.getInsurance_expiry_date().isBefore(LocalDate.now());
            }
            else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[4])) {
                return vehicle.getRc_expiry_date().isBefore(LocalDate.now());
            }
            return false; // if reason not any of the supported ones
        }
    }

    public boolean checkAmount(String amountStr, String refid, String reason) throws SQLException {
        try {
            double amount = Double.parseDouble(amountStr);

            if (amount <= 0) {
                return false;
            }

            PreparedStatement pst;
            String amountColumn;

            // PAYMENT_REASON[0] = "Challan"
            if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[0])) {
                pst = DatabaseConfig.getConnection().prepareStatement(SELECT_BY_CHALLAN_ID);
                amountColumn = "amount";
            } else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[1])){
                pst = DatabaseConfig.getConnection().prepareStatement(SELECT_BY_EXAM_ID);
                amountColumn = "fees";
            } else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[5])) {
                return amount==200;
            } else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[2])) {
                return amount==200;
            } else if (reason.equalsIgnoreCase(AppConstants.PAYMENT_REASON[3])) {
                return amount==1000;
            } else {
                return amount==500;
            }

            int refID = Integer.parseInt(refid);
            pst.setInt(1, SessionUtils.getCurrentUser().getUser_id());
            pst.setInt(2, refID);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double dbAmount = rs.getDouble(amountColumn);
                return dbAmount == amount;
            }
            return false;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}