package main.controllers;

import javafx.application.Platform;
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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UpdateProfileDetailsController implements Initializable {
    @FXML private AnchorPane sidebarPane;
    @FXML private AnchorPane adminSidebarPane;
    @FXML private TextField getUserNameField;
    @FXML private TextField getNameField;
    @FXML private TextField getDateOfBirthField;
    @FXML private TextField getAddressField;
    @FXML private TextField getEmailField;
    @FXML private TextField getPhoneField;
    @FXML private TextField getPasswordField;
    @FXML private ChoiceBox<String> getState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            if (SessionUtils.isUser()) {
                adminSidebarPane.setVisible(false);
                sidebarPane.setVisible(true);
                Button profileButton = (Button) sidebarPane.lookup("#viewProfileSettingsButton");
                if (profileButton != null) {
                    profileButton.getStyleClass().add("selected-sidebar-button");
                }
            }
            else {
                adminSidebarPane.setVisible(true);
                sidebarPane.setVisible(false);
                Button profileButton = (Button) adminSidebarPane.lookup("#viewProfileDetailsButton");
                if (profileButton != null) {
                    profileButton.getStyleClass().add("selected-sidebar-button");
                }
            }
        });

        // populates the getState choice box with state names
        getState.getItems().addAll(AppConstants.STATES_NAMES);
    }

    public void confirmUpdate(ActionEvent event) throws IOException, SQLException {
        // fetches all the inputs into variables
        String username = getUserNameField.getText();
        String name = getNameField.getText();
        String dob = getDateOfBirthField.getText();
        String address = getAddressField.getText();
        String email = getEmailField.getText();
        String phone = getPhoneField.getText();
        String password = getPasswordField.getText();
        // fetches the current user
        User user = SessionUtils.getCurrentUser();

        // Settings default value if no new data given
        if(username.isEmpty()){
            username = user.getUsername();
        }
        if (password.isEmpty()) {
            password = PasswordUtils.unhashedPassword(user.getPasswordHash());
        }
        if (name.isEmpty()) {
            name = user.getName();
        }
        if (getDateOfBirthField.getText().isEmpty()) {
            dob = user.getDob().format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT));
        }
        if (address.isEmpty()) {
            address = user.getAddress();
        }
        if (email.isEmpty()) {
            email = user.getEmail();
        }
        if (phone.isEmpty()) {
            phone = user.getPhone();
        }
        if (getState.getValue()==null) {
            getState.setValue(user.getState());
        }

        // validating new data
        if(!ValidationUtils.isValidEmail(email)){
            CustomAlert ca = new CustomAlert("Error Message", "Email Field Must Contrains @ and Must Ends With .com !!");
            ca.showAndWait();
        }else if(!ValidationUtils.isValidPhone(phone)){
            CustomAlert ca = new CustomAlert("Error Message", "Phone Number Must Contains Ten Numbers only !!");
            ca.showAndWait();
        }else if(!ValidationUtils.isValidUsername(username)){
            CustomAlert ca = new CustomAlert("Error Message", "Username Must Not Contain Special Symbols !!");
            ca.showAndWait();
        }else if(!ValidationUtils.isValidPassword(password)){
            CustomAlert ca = new CustomAlert("Error Message", "Password Must Contains Minimum 8 characters, at least 1 letter and 1 number !!");
            ca.showAndWait();
        }else if(!ValidationUtils.isValidName(name)) {
            CustomAlert ca = new CustomAlert("Error Message", "Name must be at least 3 letters long and contain only alphabets and spaces !!");
            ca.showAndWait();
        } else if (!ValidationUtils.isValidDOB(dob)) {
            CustomAlert ca = new CustomAlert("Error Message", "Date of Birth is Invalid !!");
            ca.showAndWait();
        }
        else{
            // checks if the username or email or phone already exists
            if (AppConstants.userRepository.findByUsername(username)!=null && !username.equals(user.getUsername())) {
                CustomAlert ca = new CustomAlert("Error Message", "Username already registered!");
                ca.showAndWait();
            }
            else if (AppConstants.userRepository.findByEmail(email)!=null && !email.equals(user.getEmail())) {
                CustomAlert ca = new CustomAlert("Error Message", "Email already registered!");
                ca.showAndWait();
            }
            else if (AppConstants.userRepository.findByPhone(phone)!=null && !phone.equals(user.getPhone())) {
                CustomAlert ca = new CustomAlert("Error Message", "Phone number already registered!");
                ca.showAndWait();
            }
            else {
                // parses the input dd-MM-yyyy into LocalDate using the formatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
                // updates the user data in database
                AppConstants.userRepository.update(new User(user.getUser_id(), username, PasswordUtils.hashedPassword(password), user.getRole(), name, LocalDate.parse(dob, formatter), address, email, phone, getState.getValue()));
                // refetches the current user to show updated details
                SessionUtils.setCurrentUser(AppConstants.userRepository.findById(user.getUser_id()));
                // takes the user back to profile settings page
                Scene scene = ((Node)event.getSource()).getScene();
                Parent root = FXMLLoader.load(getClass().getResource("/ProfileSettings.fxml"));
                scene.setRoot(root);
            }
        }
    }
}
