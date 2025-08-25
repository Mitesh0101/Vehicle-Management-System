package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.DrivingLicense;
import main.repository.DrivingLicenseRepository;
import main.repository.EChallanRepository;
import main.repository.PaymentRepository;
import main.repository.VehicleRepository;
import main.utils.SessionUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewDashboardController implements Initializable {
    @FXML private AnchorPane sidebarPane;
    @FXML private Label setRegisteredVehiclesLabel;
    @FXML private Label setPendingChallansLabel;
    @FXML private Label setLicenseStatusLabel;
    @FXML private Label setTotalPaymentsLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button dashboardBtn = (Button) sidebarPane.lookup("#viewDashboardButton");
            if (dashboardBtn != null) {
                dashboardBtn.getStyleClass().add("selected-sidebar-button");
            }
        });

        try {
            // gets the registered vehicles owned by the user
            setRegisteredVehiclesLabel.setText("" + AppConstants.vehicleRepository.findByUserId(SessionUtils.getCurrentUser().getUser_id()).size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            // gets the pending challans issued on vehicles owned by user
            setPendingChallansLabel.setText(""+ AppConstants.eChallanRepository.selectPendingChallans(SessionUtils.getCurrentUser().getUser_id()).size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            // fetches the driving license by user id from the database
            DrivingLicense license = AppConstants.drivingLicenseRepository.findByUserId(SessionUtils.getCurrentUser().getUser_id());
            // checks if the license exists
            if (license!=null) {
                // sets the license status if license exists
                setLicenseStatusLabel.setText(license.getStatus());
            }
            else {
                // sets no license on license status if no license found
                setLicenseStatusLabel.setText("No License");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            // sets the total payments amount done by user
            setTotalPaymentsLabel.setText(""+ AppConstants.paymentRepository.findTotalAmountOfChallan(SessionUtils.getCurrentUser().getUser_id()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}