package main.repository;

import main.config.AppConstants;
import main.config.DatabaseConfig;
import main.model.EChallan;
import main.utils.ChallanLinkedList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EChallanRepository extends Repository {
    private final String INSERT_CHALLAN_SQL = "Insert into e_challan(license_plate, issued_by, issued_date, due_date, amount, reason, status) values (?, ?, ?, ?, ?, ?, ?);";
    private final String UPDATE_CHALLAN_STATUS_SQL = "Update e_challan set status=? where challan_id=?";
    private final String SELECT_PENDING_CHALLANS_BY_USER_ID_SQL = "Select EC.challan_id, EC.license_plate, EC.issued_by, EC.issued_date, EC.due_date, EC.amount, EC.reason, EC.status From e_challan EC join vehicle V on EC.license_plate=V.license_plate where V.user_id=? and EC.status='" + AppConstants.CHALLAN_PENDING + "';";

    public void save(Object obj) {
        EChallan challan = (EChallan) obj;
        try {
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(INSERT_CHALLAN_SQL);
            pst.setString(1, challan.getLicense_plate());
            pst.setInt(2, challan.getIssued_by());
            pst.setDate(3, Date.valueOf(challan.getIssued_date()));
            pst.setDate(4, Date.valueOf(challan.getDue_date()));
            pst.setDouble(5, challan.getAmount());
            pst.setString(6, challan.getReason());
            pst.setString(7, challan.getStatus());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Challan not issued!");
        }
    }

    public void updateStatus(int challan_id, String status) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_CHALLAN_STATUS_SQL);
        pst.setString(1, status);
        pst.setInt(2, challan_id);
        pst.executeUpdate();
    }

    public ChallanLinkedList selectPendingChallans(int user_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_PENDING_CHALLANS_BY_USER_ID_SQL);
        pst.setInt(1, user_id);
        ResultSet rs = pst.executeQuery();
        ChallanLinkedList challans = new ChallanLinkedList();
        while (rs.next()) {
            challans.insertLast(new EChallan(rs.getInt("challan_id"), rs.getString("license_plate"), rs.getInt("issued_by"), rs.getDate("issued_date").toLocalDate(), rs.getDate("due_date").toLocalDate(), rs.getDouble("amount"), rs.getString("reason"), rs.getString("status")));
        }
        return challans;
    }
}
