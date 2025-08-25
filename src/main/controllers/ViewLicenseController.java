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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.DrivingExam;
import main.model.DrivingLicense;
import main.model.User;
import main.model.Vehicle;
import main.repository.DrivingExamRepository;
import main.repository.DrivingLicenseRepository;
import main.utils.CustomAlert;
import main.utils.SessionUtils;
import main.utils.ValidationUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ViewLicenseController implements Initializable {
    public AnchorPane sidebarPane;
    @FXML private Label setLicenseNumber;
    @FXML private Label setLicenseHolderName;
    @FXML private Label setLicenseType;
    @FXML private Label setIssueDate;
    @FXML private Label setExpiryDate;
    @FXML private Label setStatus;
    @FXML private Button takeDrivingExamButton;
    @FXML private Button takeExamButton;
    @FXML private Button renewLicenseButton;
    @FXML private AnchorPane noLicensePane;
    @FXML private AnchorPane licensePane;
    @FXML private AnchorPane takeDrivingExamPane;
    @FXML private ChoiceBox<String> examTypeChoiceBox;
    private DrivingLicense drivingLicense;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button licenseBtn = (Button) sidebarPane.lookup("#viewLicenseButton");
            if (licenseBtn != null) {
                licenseBtn.getStyleClass().add("selected-sidebar-button");
            }
        });

        // populates the exam type choice box with category names
        examTypeChoiceBox.getItems().addAll(AppConstants.CATEGORIES.values());
        try {
            // fetches the driving license by user id from database
            drivingLicense = AppConstants.drivingLicenseRepository.findByUserId(SessionUtils.getCurrentUser().getUser_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // checks if license doesn't exist
        if (drivingLicense==null) {
            licensePane.setVisible(false);
            noLicensePane.setVisible(true);
        } else {
            // sets all the license details into label
            licensePane.setVisible(true);
            noLicensePane.setVisible(false);
            setLicenseNumber.setText(drivingLicense.getLicense_number());

            User user = SessionUtils.getCurrentUser();
            setLicenseHolderName.setText(user.getName());

            // concats the category names with line breaks between them into category variable
            String category = "";
            try {
                for (String cat : AppConstants.drivingLicenseRepository.findCategoryByNumber(drivingLicense.getLicense_number())) {
                    category += "\n" + cat;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            setLicenseType.setText(category);
            setIssueDate.setText("" + drivingLicense.getIssue_date());
            setStatus.setText(drivingLicense.getStatus());

            setExpiryDate.setText("" + drivingLicense.getExpiry_date());
            if (drivingLicense.getExpiry_date().isBefore(LocalDate.now())) {
                try {
                    AppConstants.drivingLicenseRepository.updateLicenseStatus(drivingLicense.getLicense_number(), AppConstants.LICENSE_EXPIRED);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                takeExamButton.setVisible(false);
                renewLicenseButton.setVisible(true);
                setStatus.setText(AppConstants.LICENSE_EXPIRED);
            }

        }
    }

    // takes user to take driving exam page if take exam button clicked
    public void takeExam(ActionEvent event) throws IOException {
        takeDrivingExamPane.setVisible(true);
        licensePane.setVisible(false);
        noLicensePane.setVisible(false);
    }

    public void renewLicense(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/makePayment.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(loader.load());
        MakePaymentController controller=loader.getController();
        controller.amountField.setText("200");
        controller.referenceIdField.setText(drivingLicense.getLicense_number());
        // PAYMENT_REASON[5] = "License Renewal"
        controller.paymentReasonChoiceBox.setValue(AppConstants.PAYMENT_REASON[5]);
    }

    public void submit(ActionEvent event) throws IOException, SQLException {
        // checks if exam type chosen or not
        if(examTypeChoiceBox.getValue()==null || examTypeChoiceBox.getValue().isEmpty()){
            CustomAlert ca=new CustomAlert("Error","Please Select Type of driving license!");
            ca.showAndWait();
        }
        else {
            // fetches the driving license by user id from the database
            DrivingLicense drivingLicense = AppConstants.drivingLicenseRepository.findByUserId(SessionUtils.getCurrentUser().getUser_id());
            // checks if driving license found
            if (drivingLicense!=null) {
                // checks if user already has license of given category
                for (String category : AppConstants.drivingLicenseRepository.findCategoryByNumber(drivingLicense.getLicense_number())) {
                    if (category.equals(examTypeChoiceBox.getValue())) {
                        CustomAlert ca=new CustomAlert("Error","You already have license of this type!");
                        ca.showAndWait();
                        return;
                    }
                }
            }
            int category_id=-1;
            for (Map.Entry<Integer, String> category : AppConstants.CATEGORIES.entrySet()) {
                if (category.getValue().equals(examTypeChoiceBox.getValue())) {
                    category_id = category.getKey();
                    break;
                }
            }

            // find all exams of a user by category
            List<DrivingExam> drivingExams = AppConstants.drivingExamRepository.findByUserIdCategoryId(SessionUtils.getCurrentUser().getUser_id(), category_id);

            // Checks for existing pending slots
            for (DrivingExam drivingExam : drivingExams) {
                if (drivingExam.getResult().equals(AppConstants.RESULT_PENDING) && drivingExam.getPayment_status().equals(AppConstants.PAYMENT_PAID)) {
                    CustomAlert ca = new CustomAlert("Error Message", "You cannot book a slot because you already have a slot on " + drivingExam.getExam_date() + " for " + AppConstants.CATEGORIES.get(drivingExam.getCategory_id()) + " category");
                    ca.showAndWait();
                    return;
                }
                else if (drivingExam.getResult().equals(AppConstants.RESULT_PENDING) && drivingExam.getPayment_status().equals(AppConstants.PAYMENT_UNPAID)) {
                    CustomAlert ca = new CustomAlert("Error Message", "Slot already applied for. make payment for your slot. Exam ID : " + drivingExam.getExam_id() + " Amount : " + drivingExam.getFees());
                    ca.showAndWait();
                    // moves the user to payment page to pay for existing slot
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/makePayment.fxml"));
                    Scene scene = ((Node) event.getSource()).getScene();
                    scene.setRoot(loader.load());
                    MakePaymentController controller=loader.getController();
                    controller.referenceIdField.setText("" + drivingExam.getExam_id());
                    controller.paymentReasonChoiceBox.setValue(AppConstants.PAYMENT_REASON[1]);
                    controller.amountField.setText("1000");
                    return;
                }
            }

            // takes the user to driving exam page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/drivingExam.fxml"));
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(loader.load());
            DrivingExamController dec = loader.getController();
            // sets the category id in the driving exam controller
            dec.category_id=category_id;
        }
    }
}