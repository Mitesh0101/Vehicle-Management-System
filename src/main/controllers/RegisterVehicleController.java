package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.DrivingLicense;
import main.model.Vehicle;
import main.repository.DrivingLicenseRepository;
import main.repository.VehicleRepository;
import main.utils.CustomAlert;
import main.utils.GenerationUtils;
import main.utils.ValidationUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class RegisterVehicleController implements Initializable {
    @FXML private AnchorPane adminSidebarPane;
    @FXML private TextField getLicenseNumberField;
    @FXML private ChoiceBox<String> getCategory;
    @FXML private ChoiceBox<String> getState;
    @FXML private TextField getVehicleNameField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button searchVehicleBtn = (Button) adminSidebarPane.lookup("#registerVehicleButton");
            if (searchVehicleBtn != null) {
                searchVehicleBtn.getStyleClass().add("selected-sidebar-button");
            }
        });

        // populates the getState and getCategory choice boxes with state names and category names respectively
        getState.getItems().addAll(AppConstants.STATES_NAMES);
        getCategory.getItems().addAll(AppConstants.CATEGORIES.values());
    }

    public void registerVehicle(ActionEvent event) throws IOException, SQLException {
        // checks if license number is empty
        if (getLicenseNumberField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "License Number cannot be empty!");
            ca.showAndWait();
        }
        // checks if state is not selected
        else if (getState.getValue()==null) {
            CustomAlert ca = new CustomAlert("Error Message", "Choose a state!");
            ca.showAndWait();
        }
        // checks if category is not selected
        else if (getCategory.getValue()==null) {
            CustomAlert ca = new CustomAlert("Error Message", "Choose a category!");
            ca.showAndWait();
        }
        // checks if vehicle name is not entered
        else if (getVehicleNameField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Vehicle Name cannot be empty!");
            ca.showAndWait();
        }
        // checks if driving license number is invalid
        else if (!ValidationUtils.validateDrivingLicenseNumber(getLicenseNumberField.getText())) {
            CustomAlert ca = new CustomAlert("Error Message", "License Number invalid!");
            ca.showAndWait();
        }
        else {
            // generates a new license plate
            String license_plate = GenerationUtils.generateLicensePlate(getState.getValue());
            String license_number = getLicenseNumberField.getText();

            // fetches the driving license from the database having license number as entered
            DrivingLicense dl = AppConstants.drivingLicenseRepository.findByNumber(license_number);
            // if driving license is not found then shows CustomAlert
            if (dl==null) {
                CustomAlert ca = new CustomAlert("Error Message", "License not found!");
                ca.showAndWait();
                return;
            }
            // if driving license is not compatible with the vehicle (category of license and vehicle should be same)
            else if (!AppConstants.drivingLicenseRepository.findCategoryByNumber(license_number).contains(getCategory.getValue())) {
                CustomAlert ca = new CustomAlert("Error Message", getCategory.getValue() + " license needed!");
                ca.showAndWait();
                return;
            }

            // fetches the category id from category name
            int category_id = 0;
            for (Map.Entry<Integer, String> entry : AppConstants.CATEGORIES.entrySet()) {
                if (entry.getValue().equals(getCategory.getValue())) {
                    category_id = entry.getKey();
                }
            }
            String engine_number = GenerationUtils.generateEngineNumber();
            String chasis_number = GenerationUtils.generateChassisNumber();
            // insurance and PUC expire after 1 year and rc expire after 15 years
            LocalDate insurance_expiry_date = LocalDate.now().plusYears(1);
            LocalDate puc_expiry_date = LocalDate.now().plusYears(1);
            LocalDate rc_expiry_date = LocalDate.now().plusYears(15);
            String status = AppConstants.VEHICLE_ACTIVE;
            String vname = getVehicleNameField.getText();
            int user_id = dl.getUser_id();
            String state = getState.getValue();
            // saves the vehicle in database
            AppConstants.vehicleRepository.save(new Vehicle(license_plate, license_number, category_id, engine_number, chasis_number, insurance_expiry_date, puc_expiry_date, status, vname, user_id, state, rc_expiry_date));
            CustomAlert ca = new CustomAlert("Info Message", "Vehicle Registered Successfully!");
            ca.showAndWait();

            // clears the input fields
            getLicenseNumberField.clear();
            getVehicleNameField.clear();
            getCategory.setValue(null);
            getState.setValue(null);
        }
    }
}
