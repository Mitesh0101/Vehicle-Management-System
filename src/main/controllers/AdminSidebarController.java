package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import main.utils.SessionUtils;

import java.io.IOException;

public class AdminSidebarController {
    @FXML private Button homeButton;
    @FXML private Button searchLicenseButton;
    @FXML private Button searchVehicleButton;
    @FXML private Button issueChallanButton;
    @FXML private Button viewProfileDetailsButton;
    @FXML private Button registerVehicleButton;
    @FXML private Button logoutButton;

    private Scene scene;

    public void handleHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AdminHome.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleViewProfile(ActionEvent event) throws IOException {
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

    public void handleSearchVehicle(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/searchVehicle.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleSearchLicense(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/searchLicense.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleIssueChallan(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/issueChallan.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleRegisterVehicle(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/registerVehicle.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void handleSetExamResults(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/setExamResults.fxml"));
        scene = ((Node)event.getSource()).getScene();
        scene.setRoot(root);
    }
}
