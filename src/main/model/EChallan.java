package main.model;

import java.time.LocalDate;

public class EChallan {
    private int challan_id;
    private String license_plate;
    private int issued_by;
    private LocalDate issued_date;
    private LocalDate due_date;
    private double amount;
    private String reason;
    private String status;

    public EChallan() {}

    public EChallan(int challan_id, String license_plate, int issued_by, LocalDate issued_date, LocalDate due_date, double amount, String reason, String status) {
        this.challan_id = challan_id;
        this.license_plate = license_plate;
        this.issued_by = issued_by;
        this.issued_date = issued_date;
        this.due_date = due_date;
        this.amount = amount;
        this.reason = reason;
        this.status = status;
    }

    public int getChallan_id() {
        return challan_id;
    }

    public void setChallan_id(int challan_id) {
        this.challan_id = challan_id;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public int getIssued_by() {
        return issued_by;
    }

    public void setIssued_by(int issued_by) {
        this.issued_by = issued_by;
    }

    public LocalDate getIssued_date() {
        return issued_date;
    }

    public void setIssued_date(LocalDate issued_date) {
        this.issued_date = issued_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
