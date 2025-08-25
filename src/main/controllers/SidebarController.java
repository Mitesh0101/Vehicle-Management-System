package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.utils.SessionUtils;

import java.io.IOException;

public class SidebarController {
    @FXML private Button viewDashboardButton;
    @FXML private Button viewLicenseButton;
    @FXML private Button viewVehiclesButton;
    @FXML private Button viewChallansButton;
    @FXML private Button processPaymentButton;
    @FXML private Button viewProfileSettingsButton;
    @FXML private Button logoutButton;

    private Scene scene;

    public void handleViewDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/viewDashboard.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleViewLicense(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/viewLicense.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleViewVehicles(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/viewVehicleDetails.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleViewChallans(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/viewChallanDetails.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleMakePayment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/makePayment.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleProfileSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ProfileSettings.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        SessionUtils.clearSession();
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }
}
