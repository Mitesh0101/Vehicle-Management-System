package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.Vehicle;
import main.repository.VehicleRepository;
import main.utils.SessionUtils;
import main.utils.VehicleLinkedList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ViewVehicleDetailsController implements Initializable {
    @FXML private AnchorPane sidebarPane;
    @FXML private AnchorPane vehicleDetailsPane;
    @FXML private AnchorPane noVehiclesFoundPane;
    @FXML private Button viewNextVehicleDetails;
    @FXML private Button viewPreviousVehicleDetails;
    @FXML private Button renewPUCButton;
    @FXML private Button renewRCButton;
    @FXML private Button renewInsuranceButton;

    @FXML private Label setLicensePlate;
    @FXML private Label setName;
    @FXML private Label setLicenseNumber;
    @FXML private Label setLicenseCategory;
    @FXML private Label setLicenseEngineNumber;
    @FXML private Label setChasisNumber;
    @FXML private Label setInsuranceExpiryDate;
    @FXML private Label setPUCExpiryDate;
    @FXML private Label setRCExpiryDate;
    @FXML private Label setStatus;
    @FXML private Label setState;

    // Contains list of vehicles
    private VehicleLinkedList vehicles;
    // used for iterating over the list in both forward and backward direction
    private VehicleLinkedList.VehicleLinkedListIterator itr;
    // Current Vehicle Object
    private Vehicle current_vehicle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button vehiclesBtn = (Button) sidebarPane.lookup("#viewVehiclesButton");
            if (vehiclesBtn != null) {
                vehiclesBtn.getStyleClass().add("selected-sidebar-button");
            }
        });

        try {
            // fetches the current user's vehicles into VehicleLinkedList
            vehicles = AppConstants.vehicleRepository.findByUserId(SessionUtils.getCurrentUser().getUser_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // checks if vehicles list is empty
        if (!vehicles.isEmpty()) {
            vehicleDetailsPane.setVisible(true);
            noVehiclesFoundPane.setVisible(false);
            // if not empty then gets the iterator
            itr = vehicles.getIterator();
            // as list not empty no need to check hasNext() for first element
            current_vehicle = itr.next();
            // sets vehicle data
            setVehicleData(current_vehicle);
            // if no more vehicles available to display then next button is made not visible
            if (!itr.hasNext()) {
                viewNextVehicleDetails.setVisible(false);
            }
        }
    }

    public void viewNextVehicleDetails(ActionEvent actionEvent) {
        // sets the next vehicles data
        // no need to check hasNext as there won't be next button if no vehicles to display
        current_vehicle = itr.next();
        setVehicleData(current_vehicle);
        // if previous button was not visible previously it will be made visible
        if (!viewPreviousVehicleDetails.isVisible()) {
            viewPreviousVehicleDetails.setVisible(true);
        }
        // if no more vehicles to display then next button made invisible
        if (!itr.hasNext()) {
            viewNextVehicleDetails.setVisible(false);
        }
    }

    public void viewPreviousVehicleDetails(ActionEvent actionEvent) {
        // fetches the previous vehicle's data
        // no need to check hasPrevious as there won't be a previous button if no previous vehicles to display
        current_vehicle = itr.previous();
        setVehicleData(current_vehicle);
        // makes the next button visible if it was not visible
        if (!viewNextVehicleDetails.isVisible()) {
            viewNextVehicleDetails.setVisible(true);
        }
        // if no previous vehicles to display then previous button made invisible
        if (!itr.hasPrevious()) {
            viewPreviousVehicleDetails.setVisible(false);
        }
    }

    public void renewPUC(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/makePayment.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(loader.load());
        MakePaymentController controller=loader.getController();
        controller.amountField.setText("200");
        controller.referenceIdField.setText(current_vehicle.getLicense_plate());
        // PAYMENT_REASON[2] = "PUC"
        controller.paymentReasonChoiceBox.setValue(AppConstants.PAYMENT_REASON[2]);
    }

    public void renewRC(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/makePayment.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(loader.load());
        MakePaymentController controller=loader.getController();
        controller.amountField.setText("500");
        controller.referenceIdField.setText(current_vehicle.getLicense_plate());
        // PAYMENT_REASON[4] = "RC"
        controller.paymentReasonChoiceBox.setValue(AppConstants.PAYMENT_REASON[4]);
    }

    public void renewInsurance(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/makePayment.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(loader.load());
        MakePaymentController controller=loader.getController();
        controller.amountField.setText("1000");
        controller.referenceIdField.setText(current_vehicle.getLicense_plate());
        // PAYMENT_REASON[3] = "Insurance"
        controller.paymentReasonChoiceBox.setValue(AppConstants.PAYMENT_REASON[3]);
    }

    // sets the vehicle data
    public void setVehicleData(Vehicle vehicle) {
        // If PUC is expired then show button to renewPUC
        if (vehicle.getPuc_expiry_date().isBefore(LocalDate.now())) {
            renewPUCButton.setVisible(true);
        } else {
            renewPUCButton.setVisible(false);
        }
        // If Insurance is expired then show button to renew Insurance
        if (vehicle.getInsurance_expiry_date().isBefore(LocalDate.now())) {
            renewInsuranceButton.setVisible(true);
        } else {
            renewInsuranceButton.setVisible(false);
        }
        setStatus.setText(vehicle.getStatus());
        // If RC is expired then show button to renew RC
        if (vehicle.getRc_expiry_date().isBefore(LocalDate.now())) {
            // updates the vehicle status in database if rc expired
            try {
                AppConstants.vehicleRepository.updateStatus(vehicle.getLicense_plate(), AppConstants.VEHICLE_EXPIRED);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            setStatus.setText(AppConstants.VEHICLE_EXPIRED);
            renewRCButton.setVisible(true);
        } else {
            renewRCButton.setVisible(false);
        }
        setLicensePlate.setText(vehicle.getLicense_plate());
        setLicenseNumber.setText(vehicle.getLicense_number());
        // gets the vehicle category name
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
        setName.setText(vehicle.getVname());
        setState.setText(vehicle.getState());
    }
}
