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
import main.model.Vehicle;
import main.repository.VehicleRepository;
import main.utils.CustomAlert;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SearchVehicleController implements Initializable {
    @FXML private AnchorPane adminSidebarPane;
    @FXML private AnchorPane vehicleDetailsPane;
    @FXML private AnchorPane searchVehiclePane;
    @FXML private TextField getLicensePlateField;
    @FXML private Label setLicensePlate;
    @FXML private Label setLicenseNumber;
    @FXML private Label setLicenseCategory;
    @FXML private Label setLicenseEngineNumber;
    @FXML private Label setChasisNumber;
    @FXML private Label setInsuranceExpiryDate;
    @FXML private Label setPUCExpiryDate;
    @FXML private Label setRCExpiryDate;
    @FXML private Label setStatus;
    @FXML private Label setState;
    @FXML private Label setName;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button searchVehicleBtn = (Button) adminSidebarPane.lookup("#searchVehicleButton");
            if (searchVehicleBtn != null) {
                searchVehicleBtn.getStyleClass().add("selected-sidebar-button");
            }
        });
    }

    public void searchVehicle(ActionEvent event) throws IOException, SQLException {
        String licensePlate = getLicensePlateField.getText();
        // checks if the license plate is empty
        if (licensePlate.isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "License Plate cannot be empty!");
            ca.showAndWait();
        }
        else {
            // fetches the vehicle by license plate from the database
            Vehicle vehicle = AppConstants.vehicleRepository.findByLicensePlate(licensePlate);
            // checks if the vehicle is found
            if (vehicle!=null) {
                // if vehicle is found then it's details are shown
                vehicleDetailsPane.setVisible(true);
                searchVehiclePane.setVisible(false);
                setLicensePlate.setText(vehicle.getLicense_plate());
                setLicenseNumber.setText(vehicle.getLicense_number());
                // fetches the vehicle category by license plate using VehicleRepository
                try {
                    setLicenseCategory.setText(AppConstants.vehicleRepository.getVehicleCategory(vehicle.getLicense_plate()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                setLicenseEngineNumber.setText(vehicle.getEngine_number());
                setChasisNumber.setText(vehicle.getChassis_number());
                setInsuranceExpiryDate.setText(vehicle.getInsurance_expiry_date().toString());
                setPUCExpiryDate.setText(vehicle.getPuc_expiry_date().toString());
                setRCExpiryDate.setText(vehicle.getRc_expiry_date().toString());
                setStatus.setText(vehicle.getStatus());
                // updates the vehicle status in database if rc expired
                if (vehicle.getRc_expiry_date().isBefore(LocalDate.now())) {
                    try {
                        AppConstants.vehicleRepository.updateStatus(vehicle.getLicense_plate(), AppConstants.VEHICLE_EXPIRED);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    setStatus.setText(AppConstants.VEHICLE_EXPIRED);
                }
                setName.setText(vehicle.getVname());
                setState.setText(vehicle.getState());
            }
            else {
                CustomAlert ca = new CustomAlert("Error Message", "Vehicle not found!");
                ca.showAndWait();
            }
        }
    }

    public void goBackToSearch(ActionEvent event) {
        // goes back to search page and clears all inputs
        getLicensePlateField.clear();
        vehicleDetailsPane.setVisible(false);
        searchVehiclePane.setVisible(true);
    }
}
