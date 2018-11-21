package com.prj666_183a06.xbudget.model;

public class PlanObj {
    private String type;
    private String title;
    private double amount;
    private String period;

    public PlanObj(String type, String title, double amount, String period) {
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
