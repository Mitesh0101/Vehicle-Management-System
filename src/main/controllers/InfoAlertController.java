package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoAlertController {
    @FXML private Label title;
    @FXML private Label message;
    private Stage stage;

    public void initialize(Stage stage) {
        this.stage = stage;
    }
    // used to set the title
    public void setTitle(String toSettitle) {
        title.setText(toSettitle);
    }
    // used to set the message
    public void setMessage(String toSetmessage) {
        message.setText(toSetmessage);
    }
    // used to show the alert and wait for the user to click the OK button
    public void showAndWait() throws IOException {
        stage.show();
    }

    // close the alert if user clicks OK button
    public void okAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
