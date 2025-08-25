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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.model.User;
import main.utils.SessionUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileSettingsController implements Initializable {
    @FXML private AnchorPane sidebarPane;
    @FXML private AnchorPane adminSidebarPane;
    @FXML private Label setUserID;
    @FXML private Label setUserName;
    @FXML private Label setName;
    @FXML private Label setDateOfBirth;
    @FXML private Label setAddress;
    @FXML private Label setPhone;
    @FXML private Label setEmail;
    @FXML private Label setState;

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

        // sets all the details of current user in the labels
        User user = SessionUtils.getCurrentUser();
        setUserID.setText(user.getUser_id() + "");
        setUserName.setText(user.getUsername());
        setName.setText(user.getName());
        setDateOfBirth.setText(user.getDob().toString());
        setAddress.setText(user.getAddress());
        setEmail.setText(user.getEmail());
        setPhone.setText(user.getPhone());
        setState.setText(user.getState());
    }

    public void updateDetails(ActionEvent event) throws IOException {
        // moves user to update user details if update button is clicked
        Scene scene = ((Node)event.getSource()).getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/updateProfileDetails.fxml"));
        scene.setRoot(root);
    }
}
