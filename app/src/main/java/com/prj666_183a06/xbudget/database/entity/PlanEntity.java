package com.prj666_183a06.xbudget.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.model.Plan;

@Entity(tableName = "plan_table")
public class PlanEntity implements Plan {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "plan_id")
    private int planId;

    @ColumnInfo(name = "plan_type")
    private String planType;

    @ColumnInfo(name = "plan_title")
    private String planTitle;

    @ColumnInfo(name = "plan_amount")
    private double planAmount;

    @ColumnInfo(name = "plan_period")
    private  String planPeriod;

    public PlanEntity(String planType, String planTitle, double planAmount, String planPeriod) {
        this.planType = planType;
        this.planTitle = planTitle;
        this.planAmount = planAmount;
        this.planPeriod = planPeriod;
    }

    @NonNull
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(@NonNull int planId) {
        this.planId = planId;
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