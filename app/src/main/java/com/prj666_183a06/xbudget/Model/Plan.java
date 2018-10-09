package com.prj666_183a06.xbudget.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "plans")
public class Plan {

    public Plan(String planType, String planTitle, double planAmount, String planPeriod) {
        this.planType = planType;
        this.planTitle = planTitle;
        this.planAmount = planAmount;
        this.planPeriod = planPeriod;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "plan_type")
    private String planType;

    @ColumnInfo(name = "plan_title")
    private String planTitle;

    @ColumnInfo(name = "plan_amount")
    private double planAmount;

    @ColumnInfo(name = "plan_period")
    private  String planPeriod;


    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public double getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(double planAmount) {
        this.planAmount = planAmount;
    }

    public String getPlanPeriod() {
        return planPeriod;
    }

    public void setPlanPeriod(String planPeriod) {
        this.planPeriod = planPeriod;
    }
}
