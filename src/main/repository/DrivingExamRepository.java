package main.repository;

import main.config.AppConstants;
import main.config.DatabaseConfig;
import main.model.DrivingExam;
import main.model.DrivingExamQuestions;
import main.utils.ExamStack;
import main.utils.QuestionStack;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DrivingExamRepository extends Repository {
    private final String INSERT_EXAM_SQL = "Insert into driving_exam(user_id, exam_date, result, fees, category_id, scheduled_by, payment_status) values (?, ?, ?, ?, ?, ?, ?);";
    private final String SELECT_EXAMS_BY_USER_ID_CATEGORY_ID_SQL = "Select * From driving_exam where user_id=? and category_id=?";
    private final String UPDATE_EXAM_RESULT_SQL = "Update driving_exam set result=? where exam_id=?";
    // Selects 15 random questions of a particular category from the driving_exam_questions relation
    private final String SELECT_QUESTIONS_BY_CATEGORY_ID_SQL = "Select * from driving_exam_questions where category_id=? ORDER BY RANDOM() LIMIT 15;";
    private final String UPDATE_EXAM_PAYMENT_STATUS_SQL = "Update driving_exam set payment_status=? where exam_id=?";
    // Selects exams by officer id that are to be evaluated today with result pending and payment paid
    private final String SELECT_EXAMS_BY_OFFICER_ID = "Select * from driving_exam where scheduled_by=? and exam_date='" + LocalDate.now() + "' and result='" + AppConstants.RESULT_PENDING + "' and payment_status='" + AppConstants.PAYMENT_PAID + "';";

    @Override
    public void save(Object obj) {
        PreparedStatement pst = null;
        try {
            pst = DatabaseConfig.getConnection().prepareStatement(INSERT_EXAM_SQL);
            DrivingExam exam = (DrivingExam) obj;
            pst.setInt(1, exam.getUser_id());
            pst.setDate(2, Date.valueOf(exam.getExam_date()));
            pst.setString(3, exam.getResult());
            pst.setDouble(4, exam.getFees());
            pst.setInt(5, exam.getCategory_id());
            pst.setInt(6, exam.getScheduled_by());
            pst.setString(7, exam.getPayment_status());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateExamPaymentStatus(String payment_status, int exam_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_EXAM_PAYMENT_STATUS_SQL);
        pst.setString(1, payment_status);
        pst.setInt(2, exam_id);
        pst.executeUpdate();
    }

    public QuestionStack getQuestionsByCategory(int category_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_QUESTIONS_BY_CATEGORY_ID_SQL);
        pst.setInt(1, category_id);
        QuestionStack questions = new QuestionStack(15);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            questions.push(new DrivingExamQuestions(rs.getInt("question_id"), rs.getInt("category_id"), rs.getString("question_text"), rs.getString("option_1"), rs.getString("option_2"), rs.getString("option_3"), rs.getString("option_4"), rs.getInt("correct_answer")));
        }
        return questions;
    }

    public List<DrivingExam> findByUserIdCategoryId(int user_id, int category_id) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_EXAMS_BY_USER_ID_CATEGORY_ID_SQL);
        pst.setInt(1, user_id);
        pst.setInt(2, category_id);
        ResultSet rs = pst.executeQuery();
        List<DrivingExam> exams = new ArrayList<>();
        while (rs.next()) {
            exams.add(new DrivingExam(rs.getInt("exam_id"), rs.getInt("user_id"), rs.getDate("exam_date").toLocalDate(), rs.getString("result"), rs.getDouble("fees"), rs.getInt("category_id"), rs.getInt("scheduled_by"), rs.getString("payment_status")));
        }
        return exams;
    }

    public void updateResult(int exam_id, String result) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(UPDATE_EXAM_RESULT_SQL);
        pst.setString(1, result);
        pst.setInt(2, exam_id);
        pst.executeUpdate();
    }

    public ExamStack findExamsByOfficerID (int scheduled_by) throws SQLException {
        PreparedStatement pst = DatabaseConfig.getConnection().prepareStatement(SELECT_EXAMS_BY_OFFICER_ID);
        pst.setInt(1, scheduled_by);
        ResultSet rs = pst.executeQuery();
        ExamStack exams = new ExamStack(10);
        while (rs.next()) {
            exams.push(new DrivingExam(rs.getInt("exam_id"), rs.getInt("user_id"), rs.getDate("exam_date").toLocalDate(), rs.getString("result"), rs.getDouble("fees"), rs.getInt("category_id"), rs.getInt("scheduled_by"), rs.getString("payment_status")));
        }
        return exams;
    }
}
