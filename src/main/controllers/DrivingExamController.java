package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import main.config.AppConstants;
import main.model.DrivingExam;
import main.model.DrivingExamQuestions;
import main.model.DrivingLicense;
import main.model.User;
import main.repository.DrivingExamRepository;
import main.repository.DrivingLicenseRepository;
import main.repository.UserRepository;
import main.utils.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class DrivingExamController implements Initializable {
    @FXML private AnchorPane sidebarPane;
    @FXML private AnchorPane onlineExamPane;
    @FXML private AnchorPane offlineExamPane;
    @FXML private DatePicker examDatePicker;
    @FXML private Label setQuestion;
    @FXML private Label setTitle;
    @FXML private RadioButton option1;
    @FXML private RadioButton option2;
    @FXML private RadioButton option3;
    @FXML private RadioButton option4;
    @FXML private ToggleGroup options;
    @FXML private Button nextButton;
    @FXML private Button submitButton;
    // identified the category of which the exam is
    public int category_id;
    // keeps track of the current question
    private DrivingExamQuestions current_question;
    // Contains 15 questions of a category
    private QuestionStack questionsList;
    // counts the marks
    private int marks=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Button licenseBtn = (Button) sidebarPane.lookup("#viewLicenseButton");
            if (licenseBtn != null) {
                licenseBtn.getStyleClass().add("selected-sidebar-button");
            }
            try {
                // fetches the questions into questionsList stack
                questionsList = AppConstants.drivingExamRepository.getQuestionsByCategory(category_id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // fetches the first question into current question
            current_question = questionsList.pop();
            // sets the current question data
            setQuestionData(current_question);
            // sets the title of the exam like which category exam is being taken
            setTitle.setText(AppConstants.CATEGORIES.get(category_id) + " Driving License Exam");
        });
    }

    public void nextQuestion(ActionEvent event) throws IOException {
        // checks if an option is selected or not
        if (options.getSelectedToggle()==null) {
            CustomAlert ca = new CustomAlert("Error Message", "Please select an option!");
            ca.showAndWait();
            return;
        }
        checkAnswer(); // gives marks according to answer
        // sets the next question object in current question
        current_question = questionsList.pop();
        // sets the current question data
        setQuestionData(current_question);
        // checks if all questions fetched and if they are fetched then shows the submit button
        if (questionsList.isEmpty()) {
            nextButton.setVisible(false);
            submitButton.setVisible(true);
        }
        // resets the options toggle so no option is selected by default
        options.getSelectedToggle().setSelected(false);
    }

    public void submit(ActionEvent event) throws IOException, SQLException {
        // checks if option is selected or not
        if (options.getSelectedToggle()==null) {
            CustomAlert ca = new CustomAlert("Error Message", "Please select an option!");
            ca.showAndWait();
            return;
        }
        // checks answer of the final question
        checkAnswer();
        // checks if the user is pass or not in the online exam
        if (marks>=11) {
            // displays customalert to show that user has passed
            CustomAlert ca = new CustomAlert("Info Message", "You have passed the exam!");
            ca.showAndWait();
            // changes the anchor pane to offline exam date picker anchor pane
            offlineExamPane.setVisible(true);
            onlineExamPane.setVisible(false);
        }
        else {
            // displays CustomAlert to show that user has failed the exam
            CustomAlert ca = new CustomAlert("Info Message", "You have failed the exam!");
            ca.showAndWait();
            // moves the user to veiwLicense section
            Parent root = FXMLLoader.load(getClass().getResource("/viewLicense.fxml"));
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        }
    }

    // handles the offline exam slot booking
    public void submitOffline(ActionEvent event) throws IOException, SQLException {
        /*
        --------------------Already Implemented in viewLicenseController-----------------------------
        // find all exams of a user by category
        List<DrivingExam> drivingExams = DrivingExamRepository.findByUserIdCategoryId(SessionUtils.getCurrentUser().getUser_id(), category_id);

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
         */

        if (examDatePicker.getValue()==null) {
            CustomAlert ca = new CustomAlert("Error Message", "Please select a date !!");
            ca.showAndWait();
            return;
        }

        // fetches the date selected by user, tomorrow's date and date of one week from tomorrow
        LocalDate date = examDatePicker.getValue();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate oneWeekFromTomorrow = tomorrow.plusDays(7);

        // checks if the date is valid (between tomorrow and one week from tomorrow)
        if (date.isBefore(tomorrow) || date.isAfter(oneWeekFromTomorrow)) {
            // displays CustomAlert telling the range of date to be picked from
            CustomAlert ca = new CustomAlert("Error Message", "Please select a date between " + tomorrow + " and " + oneWeekFromTomorrow);
            ca.showAndWait();
        }
        else {
            // gets id of an officer chosen by random
            List<User> officers = AppConstants.userRepository.findAllOfficers();
            Random random = new Random();
            int officer_id = officers.get(random.nextInt(officers.size())).getUser_id();

            // creates the driving exam record which will be updated by the officer later.
            AppConstants.drivingExamRepository.save(new DrivingExam(1, SessionUtils.getCurrentUser().getUser_id(), date, AppConstants.RESULT_PENDING, 1000, category_id, officer_id, AppConstants.PAYMENT_UNPAID));

            // moves the user to payment section
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/makePayment.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(loader.load());
            MakePaymentController controller=loader.getController();
            // get reference id for the newly created driving exam record
            List<DrivingExam> drivingExams = AppConstants.drivingExamRepository.findByUserIdCategoryId(SessionUtils.getCurrentUser().getUser_id(), category_id);
            for (DrivingExam drivingExam : drivingExams) {
                if (drivingExam.getResult().equals(AppConstants.RESULT_PENDING)) {
                    controller.referenceIdField.setText("" + drivingExam.getExam_id());
                    break;
                }
            }
            // PAYMENT_REASON[1] = "Driving Exam"
            controller.paymentReasonChoiceBox.setValue(AppConstants.PAYMENT_REASON[1]);
            controller.amountField.setText("1000");
        }
    }

    // sets the question data by taking question object as input
    private void setQuestionData(DrivingExamQuestions question) {
        setQuestion.setText(question.getQuestion());
        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        option4.setText(question.getOption4());
    }

    // checks if the answer of current question is correct and gives marks accordingly
    private void checkAnswer() {
        String answer = ((RadioButton)options.getSelectedToggle()).getText();
        String correct_answer = "";
        switch (current_question.getCorrectAnswer()) {
            case 1:
                correct_answer = option1.getText();
                break;

            case 2:
                correct_answer = option2.getText();
                break;

            case 3:
                correct_answer = option3.getText();
                break;

            case 4:
                correct_answer = option4.getText();
                break;
        }
        if (answer.equals(correct_answer)) {
            marks++;
        }
    }
}
