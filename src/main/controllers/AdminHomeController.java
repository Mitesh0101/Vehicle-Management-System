package main.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.repository.*;
import main.utils.SessionUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminHomeController implements Initializable {
    @FXML private AnchorPane adminSidebarPane;
    @FXML private Label setTotalRegisteredVehicles;
    @FXML private Label setNumberOfExams;
    @FXML private Label setTotalLicensesIssued;
    @FXML private Label setTotalPayments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button searchVehicleBtn = (Button) adminSidebarPane.lookup("#homeButton");
            if (searchVehicleBtn != null) {
                searchVehicleBtn.getStyleClass().add("selected-sidebar-button");
            }
        });

        try {
            // sets all the label's text
            setTotalRegisteredVehicles.setText("" + AppConstants.vehicleRepository.countTotalVehicles());
            setNumberOfExams.setText("" + AppConstants.drivingExamRepository.findExamsByOfficerID(SessionUtils.getCurrentUser().getUser_id()).size());
            setTotalLicensesIssued.setText("" + AppConstants.drivingLicenseRepository.getTotalLicensesCount());
            setTotalPayments.setText("" + AppConstants.paymentRepository.getTotalPaymentsAmount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
