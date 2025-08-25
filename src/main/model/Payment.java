package main.model;

import java.time.LocalDate;

public class Payment {
    private int payment_id;
    private String payment_for;
    private String ref_id;
    private double amount;
    private LocalDate payment_date;
    private String mode;

    public Payment() {}

    public Payment(int payment_id, String payment_for, String ref_id, double amount, LocalDate payment_date, String mode) {
        this.payment_id = payment_id;
        this.payment_for = payment_for;
        this.ref_id = ref_id;
        this.amount = amount;
        this.payment_date = payment_date;
        this.mode = mode;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_for() {
        return payment_for;
    }

    public void setPayment_for(String payment_for) {
        this.payment_for = payment_for;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDate payment_date) {
        this.payment_date = payment_date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
