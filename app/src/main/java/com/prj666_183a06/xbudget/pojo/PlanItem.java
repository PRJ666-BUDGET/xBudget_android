package com.prj666_183a06.xbudget.pojo;

/**
 * Created by chanhwan on 2018-10-03.
 */

public class PlanItem {

    private String type;
    private String title;
    private String amount;
    private String period;
    private int image;

    public PlanItem(String type, String title, String amount, String period) {
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
