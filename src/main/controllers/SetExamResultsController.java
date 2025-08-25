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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.DrivingExam;
import main.model.DrivingLicense;
import main.model.User;
import main.repository.DrivingExamRepository;
import main.repository.DrivingLicenseRepository;
import main.repository.UserRepository;
import main.utils.CustomAlert;
import main.utils.ExamStack;
import main.utils.GenerationUtils;
import main.utils.SessionUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SetExamResultsController implements Initializable {
    @FXML private AnchorPane adminSidebarPane;
    @FXML private AnchorPane examsPane;
    @FXML private AnchorPane noExamsPane;
    @FXML private Label setExamID;
    @FXML private ChoiceBox<String> setExamResult;
    @FXML private Button nextButton;
    // contains all the exam the officer has to evaluate today
    private ExamStack exams;
    // tracks the current exam
    private DrivingExam current_exam;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button resultsBtn = (Button) adminSidebarPane.lookup("#setExamResults");
            if (resultsBtn != null) {
                resultsBtn.getStyleClass().add("selected-sidebar-button");
            }
            setExamResult.getItems().addAll(Arrays.asList(AppConstants.RESULT_FAIL, AppConstants.RESULT_PASS));
            try {
                // fetch exams to be checked by the officer
                exams = AppConstants.drivingExamRepository.findExamsByOfficerID(SessionUtils.getCurrentUser().getUser_id());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // checks if there are any exams to be checked today by the officer
            if (!exams.isEmpty()) {
                examsPane.setVisible(true);
                noExamsPane.setVisible(false);
                // fetches the first exam from exams stack into current_exam
                current_exam = exams.pop();
                setExamID.setText("" + current_exam.getExam_id());
            }
        });
    }

    // moves to the next exam
    public void nextExam(ActionEvent event) throws IOException, SQLException {
        // checks if exam result chosen or not
        if (setExamResult.getValue()==null) {
            CustomAlert ca=new CustomAlert("Error Message", "Set Exam Result!!");
            ca.showAndWait();
        }
        else {
            // updates the result in database
            AppConstants.drivingExamRepository.updateResult(current_exam.getExam_id(), setExamResult.getValue());
            // if result is pass then driving license is issued
            if (setExamResult.getValue().equals(AppConstants.RESULT_PASS)) {
                String license_number;
                // fetches the user object as state field is required for generation of driving license number
                User user = AppConstants.userRepository.findById(current_exam.getUser_id());
                DrivingLicense dl = AppConstants.drivingLicenseRepository.findByUserId(user.getUser_id());
                // generates a new license number or uses an existing one depending on if user already has driving license
                if (dl==null) {
                    license_number = GenerationUtils.generateLicenseNumber(user.getState());
                }
                else {
                    license_number = dl.getLicense_number();
                }
                // issues the license
                AppConstants.drivingLicenseRepository.issueLicense(license_number, user.getUser_id(), current_exam.getCategory_id());
            }

            // checks if all the exams are evaluated
            if (exams.isEmpty()) {
                // if all exams evaluated then shows a CustomAlert and takes the officer back to home page
                CustomAlert ca = new CustomAlert("Info Message", "All Exams Evaluated!");
                ca.showAndWait();
                Parent root = FXMLLoader.load(getClass().getResource("/AdminHome.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                scene.setRoot(root);
                return;
            }

            // sets the current_exam to next exam in the stack and sets the exam id in label
            current_exam = exams.pop();
            setExamID.setText("" + current_exam.getExam_id());
        }
    }
}
