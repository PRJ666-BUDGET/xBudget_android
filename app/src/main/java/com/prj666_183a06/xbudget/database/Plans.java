package com.prj666_183a06.xbudget.database;

import java.util.HashMap;
import java.util.Map;

public class Plans {

    private String plan_type;
    private String plan_title;
    private double plan_amount;
    private String plan_period;

    public Plans(){

    }

    public Plans(String plan_type, String plan_title, double plan_amount, String plan_period) {
        this.plan_type = plan_type;
        this.plan_title = plan_title;
        this.plan_amount = plan_amount;
        this.plan_period = plan_period;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public String getPlan_title() {
        return plan_title;
    }

    public double getPlan_amount() {
        return plan_amount;
    }

    public String getPlan_period() {
        return plan_period;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("plan_type", plan_type);
        result.put("plan_title", plan_title);
        result.put("plan_amount", plan_amount);
        result.put("plan_period", plan_period);

        return result;
    }
}
