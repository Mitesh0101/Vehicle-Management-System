package main.model;

import java.time.LocalDate;

public class DrivingExam {
    private int exam_id;
    private int user_id;
    private LocalDate exam_date;
    private String result;
    private double fees;
    private int category_id;
    private int scheduled_by;
    private String payment_status;

    public DrivingExam() {}

    public DrivingExam(int exam_id, int user_id, LocalDate exam_date, String result, double fees, int category_id, int scheduled_by, String payment_status) {
        this.exam_id = exam_id;
        this.user_id = user_id;
        this.exam_date = exam_date;
        this.result = result;
        this.fees = fees;
        this.category_id = category_id;
        this.scheduled_by = scheduled_by;
        this.payment_status = payment_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public int getScheduled_by() {
        return scheduled_by;
    }

    public void setScheduled_by(int scheduled_by) {
        this.scheduled_by = scheduled_by;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDate getExam_date() {
        return exam_date;
    }

    public void setExam_date(LocalDate exam_date) {
        this.exam_date = exam_date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }
}
