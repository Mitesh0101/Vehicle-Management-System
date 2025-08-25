package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.config.AppConstants;
import main.model.User;
import main.repository.UserRepository;
import main.utils.CustomAlert;
import main.utils.PasswordUtils;
import main.utils.ValidationUtils;
import main.utils.SessionUtils;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField getUsernameField;
    @FXML private PasswordField getPasswordField;

    public void register(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Register.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void login(ActionEvent event) throws IOException, SQLException {
        // gets the username and password field into username and password variables
        String username = getUsernameField.getText();
        String password = getPasswordField.getText();
        // checks if username is empty
        if (username.isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Username cannot be empty!");
            ca.showAndWait();
        }
        // checks if password is empty
        else if (password.isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Password cannot be empty!");
            ca.showAndWait();
        }
        else {
            // checks if username is valid (contains only letters and digits)
            if (!ValidationUtils.isValidUsername(username)) {
                CustomAlert ca = new CustomAlert("Error Message", "Invalid Username");
                ca.showAndWait();
            }
            // checks if password is valid (contains both letter and digit)
            else if (!ValidationUtils.isValidPassword(password)) {
                CustomAlert ca = new CustomAlert("Error Message", "Invalid Password");
                ca.showAndWait();
            }
            else {
                // fetches the user from the database with the username entered by user
                User currentUser = AppConstants.userRepository.findByUsername(username);
                // checks if user not found
                if (currentUser==null) {
                    CustomAlert ca = new CustomAlert("Error Message", "Username not found!");
                    ca.showAndWait();
                }
                // checks if user has entered right password
                else if (currentUser.getPasswordHash().equals(PasswordUtils.hashedPassword(password))) {
                    CustomAlert ca = new CustomAlert("Info Message", "Successfully Logged in!");
                    ca.showAndWait();
                    // sets the current user of current session
                    SessionUtils.setCurrentUser(currentUser);
                    // checks if user is officer
                    if (SessionUtils.isOfficer()) {
                        // opens admin home if user is officer
                        Parent root = FXMLLoader.load(getClass().getResource("/AdminHome.fxml"));
                        Scene scene = ((Node) event.getSource()).getScene();
                        scene.setRoot(root);
                    }
                    else {
                        // opens view dashboard if user is not officer
                        Parent root = FXMLLoader.load(getClass().getResource("/viewDashboard.fxml"));
                        Scene scene = ((Node) event.getSource()).getScene();
                        scene.setRoot(root);
                    }
                }
                else {
                    // displays error message if password is incorrect
                    CustomAlert ca = new CustomAlert("Error Message", "Password incorrect!");
                    ca.showAndWait();
                }
            }
        }
    }
}
