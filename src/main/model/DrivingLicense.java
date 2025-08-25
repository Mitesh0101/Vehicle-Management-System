package main.model;
import java.time.LocalDate;

public class DrivingLicense {
    private String license_number;
    private int user_id;
    private LocalDate issue_date;
    private LocalDate expiry_date;
    private String status;

    public DrivingLicense() {}

    public DrivingLicense(String license_number, int user_id, LocalDate issue_date, LocalDate expiry_date, String status) {
        this.license_number = license_number;
        this.user_id = user_id;
        this.issue_date = issue_date;
        this.expiry_date = expiry_date;
        this.status = status;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDate getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(LocalDate issue_date) {
        this.issue_date = issue_date;
    }

    public LocalDate getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(LocalDate expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
