package main.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controllers.InfoAlertController;

import java.io.IOException;

public class CustomAlert {
    private String title;
    private String message;
    static Stage ownerStage;

    public CustomAlert(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public static void setOwnerStage(Stage ownerStage) {
        CustomAlert.ownerStage = ownerStage;
    }

    public void showAndWait() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/infoAlert.fxml"));
        Parent root = loader.load();
        InfoAlertController iac = loader.getController();
        iac.setTitle(title);
        iac.setMessage(message);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        iac.initialize(stage);
        iac.showAndWait();
    }
}
