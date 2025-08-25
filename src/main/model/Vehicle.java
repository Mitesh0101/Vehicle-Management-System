package main.model;

import java.time.LocalDate;

public class Vehicle {
    private String license_plate;
    private String license_number;
    private int category_id;
    private String engine_number;
    private String chassis_number;
    private LocalDate insurance_expiry_date;
    private LocalDate puc_expiry_date;
    private String status;
    private String vname;
    private int user_id;
    private String state;
    private LocalDate rc_expiry_date;

    public Vehicle() {}

    public Vehicle(String license_plate, String license_number, int category_id, String engine_number, String chassis_number, LocalDate insurance_expiry_date, LocalDate puc_expiry_date, String status, String vname, int user_id, String state, LocalDate rc_expiry_date) {
        this.license_plate = license_plate;
        this.license_number = license_number;
        this.category_id = category_id;
        this.engine_number = engine_number;
        this.chassis_number = chassis_number;
        this.insurance_expiry_date = insurance_expiry_date;
        this.puc_expiry_date = puc_expiry_date;
        this.status = status;
        this.vname = vname;
        this.user_id = user_id;
        this.state = state;
        this.rc_expiry_date = rc_expiry_date;
    }

    public LocalDate getRc_expiry_date() {
        return rc_expiry_date;
    }

    public void setRc_expiry_date(LocalDate rc_expiry_date) {
        this.rc_expiry_date = rc_expiry_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setEngine_number(String engine_number) {
        this.engine_number = engine_number;
    }

    public String getChassis_number() {
        return chassis_number;
    }

    public void setChassis_number(String chassis_number) {
        this.chassis_number = chassis_number;
    }

    public LocalDate getInsurance_expiry_date() {
        return insurance_expiry_date;
    }

    public void setInsurance_expiry_date(LocalDate insurance_expiry_date) {
        this.insurance_expiry_date = insurance_expiry_date;
    }

    public LocalDate getPuc_expiry_date() {
        return puc_expiry_date;
    }

    public void setPuc_expiry_date(LocalDate puc_expiry_date) {
        this.puc_expiry_date = puc_expiry_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
