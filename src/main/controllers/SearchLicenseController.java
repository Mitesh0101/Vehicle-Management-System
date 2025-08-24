package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.DrivingLicense;
import main.model.User;
import main.repository.DrivingLicenseRepository;
import main.repository.UserRepository;
import main.utils.CustomAlert;
import main.utils.SessionUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SearchLicenseController implements Initializable {
    @FXML private AnchorPane adminSidebarPane;
    @FXML private AnchorPane viewLicensePane;
    @FXML private AnchorPane searchLicensePane;
    @FXML private TextField getLicenseNumberField;
    @FXML private Label setLicenseNumber;
    @FXML private Label setLicenseHolderName;
    @FXML private Label setLicenseType;
    @FXML private Label setIssueDate;
    @FXML private Label setExpiryDate;
    @FXML private Label setStatus;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button searchVehicleBtn = (Button) adminSidebarPane.lookup("#searchLicenseButton");
            if (searchVehicleBtn != null) {
                searchVehicleBtn.getStyleClass().add("selected-sidebar-button");
            }
        });
    }

    public void searchLicense(ActionEvent event) throws IOException, SQLException {
        String licenseNumber = getLicenseNumberField.getText();
        // checks if the license number is not empty
        if (!licenseNumber.isEmpty()) {
            // fetches the driving license from the database by license number
            DrivingLicense drivingLicense = AppConstants.drivingLicenseRepository.findByNumber(licenseNumber);
            // checks if the driving license is found
            if (drivingLicense!=null) {
                // shows the license details
                viewLicensePane.setVisible(true);
                searchLicensePane.setVisible(false);
                setLicenseNumber.setText(drivingLicense.getLicense_number());
                // concats all the category names with a line break between them into category variable
                String category = "";
                try {
                    for (String cat : AppConstants.drivingLicenseRepository.findCategoryByNumber(drivingLicense.getLicense_number())) {
                        category += "\n" + cat;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                setLicenseType.setText(category);
                // gets the license holder name by fetching user by id frmo database
                setLicenseHolderName.setText(AppConstants.userRepository.findById(drivingLicense.getUser_id()).getName());
                setIssueDate.setText("" + drivingLicense.getIssue_date());
                setExpiryDate.setText("" + drivingLicense.getExpiry_date());
                setStatus.setText(drivingLicense.getStatus());
                if (drivingLicense.getExpiry_date().isBefore(LocalDate.now())) {
                    try {
                        AppConstants.drivingLicenseRepository.updateLicenseStatus(drivingLicense.getLicense_number(), AppConstants.LICENSE_EXPIRED);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    setStatus.setText(AppConstants.LICENSE_EXPIRED);
                }
            }
            else {
                CustomAlert ca = new CustomAlert("Error Message", "License not found!");
                ca.showAndWait();
            }
        }
        else {
            CustomAlert ca = new CustomAlert("Error Message", "License number cannot be empty!");
            ca.showAndWait();
        }
    }

    public void goBackToSearch(ActionEvent event) {
        // go back to search and clear the inputs
        getLicenseNumberField.clear();
        viewLicensePane.setVisible(false);
        searchLicensePane.setVisible(true);
    }
}
