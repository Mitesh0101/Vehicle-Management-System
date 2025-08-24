package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.EChallan;
import main.repository.DrivingLicenseRepository;
import main.repository.EChallanRepository;
import main.repository.VehicleRepository;
import main.utils.CustomAlert;
import main.utils.SessionUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class IssueChallanController implements Initializable {
    @FXML private AnchorPane adminSidebarPane;
    @FXML private TextField getLicensePlateField;
    @FXML private TextField getAmountField;
    @FXML private TextField getReasonField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button searchVehicleBtn = (Button) adminSidebarPane.lookup("#issueChallanButton");
            if (searchVehicleBtn != null) {
                searchVehicleBtn.getStyleClass().add("selected-sidebar-button");
            }
        });
    }

    public void issueChallan(ActionEvent event) throws IOException, SQLException {
        // checks if license plate field is empty
        if (getLicensePlateField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "License Plate cannot be empty!");
            ca.showAndWait();
        }
        // checks if amount field is empty
        else if (getAmountField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Amount cannot be empty!");
            ca.showAndWait();
        }
        // checks if reason field is empty
        else if (getReasonField.getText().isEmpty()) {
            CustomAlert ca = new CustomAlert("Error Message", "Reason cannot be empty!");
            ca.showAndWait();
        }
        else {
            // declares amount
            double amount;
            try {
                // parses the String to double
                amount = Double.parseDouble(getAmountField.getText());
                if (amount<=0) {
                    // shows CustomAlert if amount less than or equal to 0
                    CustomAlert ca = new CustomAlert("Error Message", "Amount should be greater than 0");
                    ca.showAndWait();
                    return;
                }
            }
            catch (NumberFormatException e) {
                // shows CustomAlert if amount contains characters other than digits
                CustomAlert ca = new CustomAlert("Error Message", "Amount should contain only digits");
                ca.showAndWait();
                return;
            }
            // gets the license plate field's text into license plate variable
            String licensePlate = getLicensePlateField.getText();
            // checks if license plate exists or not in the database
            if (AppConstants.vehicleRepository.findByLicensePlate(licensePlate)==null) {
                CustomAlert ca = new CustomAlert("Error Message", "License Plate not found");
                ca.showAndWait();
                return;
            }
            String reason = getReasonField.getText();
            int issued_by = SessionUtils.getCurrentUser().getUser_id();
            LocalDate issued_date = LocalDate.now();
            // gets the date after one month of issued date into due_date
            LocalDate due_date = issued_date.plusMonths(1);
            String status = AppConstants.CHALLAN_PENDING;

            // challan saved in the database
            AppConstants.eChallanRepository.save(new EChallan(1, licensePlate, issued_by, issued_date, due_date, amount, reason, status));
            CustomAlert ca = new CustomAlert("Info Message", "Challan Issued Successfully!");
            ca.showAndWait();

            // clears all the fields
            getAmountField.clear();
            getReasonField.clear();
            getLicensePlateField.clear();
        }
    }
}
