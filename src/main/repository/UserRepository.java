package main.repository;

import main.config.AppConstants;
import main.config.DatabaseConfig;
import main.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends Repository{
    private final String INSERT_USER_SQL = "Insert into users(username, password_hash, role, name, dob, address, email, phone, state) values (?,?,?,?,?,?,?,?,?);";
    private final String SELECT_USER_BY_ID_SQL = "Select * From users where user_id=?;";
    private final String SELECT_USER_BY_USERNAME_SQL = "Select * From users where username=?;";

    // Changed
    private final String SELECT_USER_BY_EMAIL_SQL = "Select * from users where email=?";
    private final String SELECT_USER_BY_PHONE_SQL = "Select * from users where phone=?";
    // Changed finish

    private final String SELECT_ALL_OFFICERS_SQL = "Select * From users where role='" + AppConstants.ROLE_OFFICER + "';";
    private final String UPDATE_USER_PROFILE_SQL = "Update users set username=?, password_hash=?, name=?, dob=?, address=?, email=?, phone=?, state=? where user_id=?";

    public void save(Object obj) {
        User user = (User) obj;
        try {
            PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(INSERT_USER_SQL);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPasswordHash());
            pst.setString(3, user.getRole());
            pst.setString(4, user.getName());
            pst.setDate(5, Date.valueOf(user.getDob()));
            pst.setString(6, user.getAddress());
            pst.setString(7, user.getEmail());
            pst.setString(8, user.getPhone());
            pst.setString(9, user.getState());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Vehicle not inserted into database");
        }
    }

    public User findById(int user_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_USER_BY_ID_SQL);
        pst.setInt(1, user_id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("role"), rs.getString("name"), rs.getDate("dob").toLocalDate(), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getString("state"));
        }
        else {
            return null;
        }
    }

    public User findByUsername(String username) throws SQLException{
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_USER_BY_USERNAME_SQL);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("role"), rs.getString("name"), rs.getDate("dob").toLocalDate(), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getString("state"));
        }
        else {
            return null;
        }
    }

    // Changed
    public User findByEmail(String email) throws SQLException{
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_USER_BY_EMAIL_SQL);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("role"), rs.getString("name"), rs.getDate("dob").toLocalDate(), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getString("state"));
        }
        else {
            return null;
        }
    }

    public User findByPhone(String phone) throws SQLException{
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_USER_BY_PHONE_SQL);
        pst.setString(1, phone);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("role"), rs.getString("name"), rs.getDate("dob").toLocalDate(), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getString("state"));
        }
        else {
            return null;
        }
    }

    public List<User> findAllOfficers() throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_ALL_OFFICERS_SQL);
        ResultSet rs = pst.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("role"), rs.getString("name"), rs.getDate("dob").toLocalDate(), rs.getString("address"), rs.getString("email"), rs.getString("phone"), rs.getString("state")));
        }
        return users;
    }

    public void update(User user) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_USER_PROFILE_SQL);
        pst.setString(1, user.getUsername());
        pst.setString(2, user.getPasswordHash());
        pst.setString(3, user.getName());
        pst.setDate(4, Date.valueOf(user.getDob()));
        pst.setString(5, user.getAddress());
        pst.setString(6, user.getEmail());
        pst.setString(7, user.getPhone());
        pst.setString(8, user.getState());
        pst.setInt(9, user.getUser_id());
        pst.executeUpdate();
    }
}
