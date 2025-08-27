package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.config.DatabaseConfig;
import main.utils.CustomAlert;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Initializing DataBase
        DatabaseConfig.initialize();

        // Loading FXML
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Setting custom alert owner stage
        CustomAlert.setOwnerStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}