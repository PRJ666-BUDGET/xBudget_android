package com.prj666_183a06.xbudget;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseJSON {
    public static String jsonToString(ExpenseObj obj){

        JSONObject j = new JSONObject();
        try {
            j.put("store", obj.getStore());
            j.put("date", obj.getDate());
            j.put("item", obj.getItem().getName());
            j.put("cost", obj.getItem().getCost());

        }catch(JSONException ex) {
            ex.printStackTrace();
        }
        return j.toString();
    }
}
