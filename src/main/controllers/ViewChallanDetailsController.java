package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.EChallan;
import main.model.User;
import main.repository.EChallanRepository;
import main.repository.UserRepository;
import main.utils.ChallanLinkedList;
import main.utils.SessionUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewChallanDetailsController implements Initializable {
    @FXML private AnchorPane sidebarPane;
    @FXML private Label setChallanId;
    @FXML private Label setLicensePlate;
    @FXML private Label setIssuedBy;
    @FXML private Label setIssuedDate;
    @FXML private Label setDueDate;
    @FXML private Label setAmount;
    @FXML private Label setReason;
    @FXML private Label setStatus;
    @FXML private AnchorPane noChallansPane;
    @FXML private AnchorPane challanDetailsPane;
    @FXML private Button viewNextChallanButton;
    @FXML private Button viewPreviousChallanButton;
    // contains the user's pending challans
    private ChallanLinkedList challans;
    // used to iterate over the challans
    private ChallanLinkedList.ChallanLinkedListIterator itr;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button challansBtn = (Button) sidebarPane.lookup("#viewChallansButton");
            if (challansBtn != null) {
                challansBtn.getStyleClass().add("selected-sidebar-button");
            }
        });

        try {
            // fetches the pending challans of user into challans linked list
            challans = AppConstants.eChallanRepository.selectPendingChallans(SessionUtils.getCurrentUser().getUser_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // checks if the challans list is empty
        if (!challans.isEmpty()) {
            noChallansPane.setVisible(false);
            challanDetailsPane.setVisible(true);
            // if not empty then iterator is fetched
            itr = challans.getIterator();
            // used itr.next() directly as for the first element no need to check as challan list not empty
            setChallanData(itr.next());
            // if no next challan to display then next button is made invisible
            if (!itr.hasNext()) {
                viewNextChallanButton.setVisible(false);
            }
        }
    }

    public void viewNextChallanDetails(ActionEvent actionEvent) {
        // sets the next challans data in the labels
        setChallanData(itr.next());
        // makes the previous button visible if it was not visible
        if (!viewPreviousChallanButton.isVisible()) {
            viewPreviousChallanButton.setVisible(true);
        }
        // if no next challans to display then next button is made invisible
        if (!itr.hasNext()) {
            viewNextChallanButton.setVisible(false);
        }
    }

    public void viewPreviousChallanDetails(ActionEvent event) {
        // sets the previous challans data in the labels
        setChallanData(itr.previous());
        // makes the next button visible if it was invisible
        if (!viewNextChallanButton.isVisible()) {
            viewNextChallanButton.setVisible(true);
        }
        // if no previous challans to display then previous button is made invisible
        if (!itr.hasPrevious()) {
            viewPreviousChallanButton.setVisible(false);
        }
    }

    // sets the challan data in the labels
    public void setChallanData(EChallan challan) {
        setChallanId.setText("" + challan.getChallan_id());
        setLicensePlate.setText(challan.getLicense_plate());
        User issued_by = null;
        try {
            // fetches the officer who issued the challan
            issued_by = AppConstants.userRepository.findById(challan.getIssued_by());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setIssuedBy.setText(issued_by.getName());
        setIssuedDate.setText(challan.getIssued_date().toString());
        setDueDate.setText(challan.getDue_date().toString());
        setAmount.setText("" + challan.getAmount());
        setReason.setText(challan.getReason());
        setStatus.setText(challan.getStatus());
    }
}
