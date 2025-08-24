package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.User;
import main.repository.UserRepository;
import main.utils.CustomAlert;
import main.utils.PasswordUtils;
import main.utils.SessionUtils;
import main.utils.ValidationUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Button SubmitButton;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private ChoiceBox<String> getState;

    @FXML
    private TextField getAddressField;

    @FXML
    private TextField getDOBField;

    @FXML
    private TextField getEmailField;

    @FXML
    private TextField getNameField;

    @FXML
    private PasswordField getPasswordField;

    @FXML
    private TextField getPhoneField;

    @FXML
    private TextField getUsernameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // populates the getState choice box with state names
        getState.getItems().addAll(AppConstants.STATES_NAMES);
    }

    public void registerUser(ActionEvent event) throws SQLException, IOException {
        // checks if the username field is empty
        if (getUsernameField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Username can not be empty!");
            ca.showAndWait();
        }
        // checks if the password field is empty
        else if (getPasswordField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Password Field can not be empty !!");
            ca.showAndWait();
        }
        // checks if the name field is empty
        else if (getNameField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Name Field can not be empty !!");
            ca.showAndWait();
        }
        // checks if the dob field is empty
        else if (getDOBField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Date Of Birth Field can not be empty !!");
            ca.showAndWait();
        }
        // checks if the address field is empty
        else if (getAddressField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Address Field can not be empty !!");
            ca.showAndWait();
        }
        // checks if the email field is empty
        else if (getEmailField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Email Field can not be empty !!");
            ca.showAndWait();
        }
        // checks if the phone field is empty
        else if (getPhoneField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Phone Number Field can not be empty !!");
            ca.showAndWait();
        }
        // checks if no state is selected
        else if (getState.getValue() == null) {
            CustomAlert ca = new CustomAlert("Error Message", "Choose a State!!");
            ca.showAndWait();
        } else {
            // checks if username is valid
            if (!ValidationUtils.isValidUsername(getUsernameField.getText())) {
                CustomAlert ca = new CustomAlert("Error Message", "Username Must Not Contain Special Symbols !!");
                ca.showAndWait();
            }
            // checks if password is valid
            else if (!ValidationUtils.isValidPassword(getPasswordField.getText())) {
                CustomAlert ca = new CustomAlert("Error Message", "Password Must Contains Minimum 8 characters, at least 1 letter and 1 number !!");
                ca.showAndWait();
            }
            // checks if name is valid
            else if (!ValidationUtils.isValidName(getNameField.getText())) {
                CustomAlert ca = new CustomAlert("Error Message", "Name must be at least 3 letters long and contain only alphabets and spaces !!");
                ca.showAndWait();
            }
            // checks if dob is valid
            else if (!ValidationUtils.isValidDOB(getDOBField.getText())) {
                CustomAlert ca = new CustomAlert("Error Message", "Please enter a valid date of birth !!");
                ca.showAndWait();
            }
            // checks if email is valid
            else if (!ValidationUtils.isValidEmail(getEmailField.getText())) {
                CustomAlert ca = new CustomAlert("Error Message", "Email Field Must Contrains @ and Must Ends With .com !!");
                ca.showAndWait();
            }
            // checks if phone is valid
            else if (!ValidationUtils.isValidPhone(getPhoneField.getText())) {
                CustomAlert ca = new CustomAlert("Error Message", "Phone Number is invalid !!");
                ca.showAndWait();
            } else {
                // checks if the username already exists in database
                if (AppConstants.userRepository.findByUsername(getUsernameField.getText()) != null) {
                    CustomAlert ca = new CustomAlert("Error Message", "Username already registered!");
                    ca.showAndWait();
                }
                // checks if the email already exists in database
                else if (AppConstants.userRepository.findByEmail(getEmailField.getText()) != null) {
                    CustomAlert ca = new CustomAlert("Error Message", "Email already registered!");
                    ca.showAndWait();
                }
                // checks if the phone already exists in database
                else if (AppConstants.userRepository.findByPhone(getPhoneField.getText()) != null) {
                    CustomAlert ca = new CustomAlert("Error Message", "Phone number already registered!");
                    ca.showAndWait();
                } else {
                    String role;
                    role = AppConstants.ROLE_USER;
                    // parses the input of the format dd-MM-yyyy to LocalDate
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
                    LocalDate dob = LocalDate.parse(getDOBField.getText(), formatter);
                    // saves the user in database
                    AppConstants.userRepository.save(new User(1, getUsernameField.getText(), PasswordUtils.hashedPassword(getPasswordField.getText()), role, getNameField.getText(), dob, getAddressField.getText(), getEmailField.getText(), getPhoneField.getText(), getState.getValue()));
                    CustomAlert ca = new CustomAlert("Registration Successfull", "You have been registered successfully. Login now.");
                    ca.showAndWait();
                    // moves the user back to login page
                    Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
                    Scene scene = ((Node) event.getSource()).getScene();
                    scene.setRoot(root);
                }
            }
        }
    }
}