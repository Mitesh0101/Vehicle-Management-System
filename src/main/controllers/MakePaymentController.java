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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.Payment;
import main.repository.*;
import main.utils.CustomAlert;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MakePaymentController implements Initializable {
    public AnchorPane sidebarPane;
    @FXML public ChoiceBox<String> paymentReasonChoiceBox;
    @FXML public TextField referenceIdField;
    @FXML public TextField amountField;
    @FXML private ChoiceBox<String> setPaymentModeChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button paymentBtn = (Button) sidebarPane.lookup("#processPaymentButton");
            if (paymentBtn != null) {
                paymentBtn.getStyleClass().add("selected-sidebar-button");
            }
            // populates the setPaymentmodeChoiceBox with payment modes
            setPaymentModeChoiceBox.getItems().addAll(AppConstants.PAYMENT_MODE);
            // populates the paymentReasonChoiceBox with payment reason
            paymentReasonChoiceBox.getItems().addAll(AppConstants.PAYMENT_REASON);
        });
    }


    public void makePayment(ActionEvent event) throws IOException, SQLException {
        // gets all the inputs in variables
        String paymentReason = paymentReasonChoiceBox.getValue();
        String referenceId = referenceIdField.getText();
        String amount = amountField.getText();
        String paymentMode = setPaymentModeChoiceBox.getValue();

        // checks if payment reason is null or empty
        if (paymentReason == null || paymentReason.isEmpty()) {
            CustomAlert ca=new CustomAlert("Error", "Payment Reason Can Not Be Empty!");
            ca.showAndWait();
        }
        // checks if ref id is null or empty
        else if (referenceId == null || referenceId.isEmpty()) {
            CustomAlert ca=new CustomAlert("Error", "Reference Id Can Not Be Empty!");
            ca.showAndWait();
        }
        // checks if the ref id exists in the database
        else if (!AppConstants.paymentRepository.checkReferenceId(paymentReason, referenceId)) {
            CustomAlert ca=new CustomAlert("Error", "Reference ID is Invalid");
            ca.showAndWait();
        }
        // checks if the amount is null or empty
        else if (amount == null || amount.isEmpty()) {
            CustomAlert ca=new CustomAlert("Error", "Amount Can Not Be Empty!");
            ca.showAndWait();
        }
        // checks if the challan or driving exam is already paid for
        else if(!AppConstants.paymentRepository.checkStatusOfChallan(paymentReason,referenceId)){
            CustomAlert ca=new CustomAlert("Error","No need for Payment for " + paymentReason);
            ca.showAndWait();
        }
        // checks if the amount is valid
        else if (!AppConstants.paymentRepository.checkAmount(amount,referenceId,paymentReason)) {
            CustomAlert ca=new CustomAlert("Error", "Amount is Invalid!");
            ca.showAndWait();
        }
        // checks if the payment mode is null or empty
        else if (paymentMode == null || paymentMode.isEmpty()) {
            CustomAlert ca=new CustomAlert("Error", "Payment Mode Can Not Be Empty!");
            ca.showAndWait();
        }
        else {
            // creates a payment object and saves the payment info in database
            Payment payment=new Payment(1,paymentReason,referenceId,Double.parseDouble(amount), LocalDate.now(),paymentMode);
            AppConstants.paymentRepository.save(payment);
            // PAYMENT_REASON[0] = "Challan"
            // checks if the payment reason is challan
            if (paymentReason.equals(AppConstants.PAYMENT_REASON[0])) {
                AppConstants.eChallanRepository.updateStatus(Integer.parseInt(referenceId),AppConstants.PAYMENT_PAID);
                CustomAlert ca=new CustomAlert("Information","Payment Successful!");
                ca.showAndWait();
            }
            // checks if the payment reason is exam
            else if (paymentReason.equals(AppConstants.PAYMENT_REASON[1])){
                AppConstants.drivingExamRepository.updateExamPaymentStatus(AppConstants.PAYMENT_PAID, Integer.parseInt(referenceId));
                CustomAlert ca=new CustomAlert("Information","Driving Exam Slot Booked Successfully");
                ca.showAndWait();
            }
            // checks if the payment reason is license renewal
            else if (paymentReason.equals(AppConstants.PAYMENT_REASON[5])) {
                AppConstants.drivingLicenseRepository.updateExpiry(referenceId, LocalDate.now().plusYears(10));
                AppConstants.drivingLicenseRepository.updateLicenseStatus(referenceId, AppConstants.LICENSE_ACTIVE);
                CustomAlert ca=new CustomAlert("Information","Driving License Renewed Successfully for license number : " + referenceId);
                ca.showAndWait();
            }
            // checks if the payment reason is PUC
            else if (paymentReason.equals(AppConstants.PAYMENT_REASON[2])) {
                AppConstants.vehicleRepository.updatePUCExpiry(referenceId, LocalDate.now().plusYears(1));
                CustomAlert ca=new CustomAlert("Information","PUC Renewed Successfully for " + referenceId);
                ca.showAndWait();
            }
            // checks if the payment reason is Insurance
            else if (paymentReason.equals(AppConstants.PAYMENT_REASON[3])) {
                AppConstants.vehicleRepository.updateInsuranceExpiry(referenceId, LocalDate.now().plusYears(1));
                CustomAlert ca=new CustomAlert("Information","Insurance Renewed Successfully for " + referenceId);
                ca.showAndWait();
            }
            // checks if the payment reason is RC
            else if (paymentReason.equals(AppConstants.PAYMENT_REASON[4])) {
                AppConstants.vehicleRepository.updateRCExpiry(referenceId, LocalDate.now().plusYears(5));
                CustomAlert ca=new CustomAlert("Information","RC Renewed Successfully for " + referenceId);
                ca.showAndWait();
            }

            // Clears all the inputs
            paymentReasonChoiceBox.setValue(null);
            referenceIdField.clear();
            amountField.clear();
            setPaymentModeChoiceBox.setValue(null);
        }
    }
}