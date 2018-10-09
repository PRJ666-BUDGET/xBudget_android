package com.prj666_183a06.xbudget;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class ExpenseObj {
    private String store;
    private String date;
    private Item item;

    //setters n getters
    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDate() {
        return date;
    }

    public Item getItem(){
        return item;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ExpenseObj(String a, String b, String c, String d){
        this.store = a;
        this.date = b;
        item = new Item(c, d);
    }

    public class Item{
        public String name;
        public String cost;

        public Item(String a, String b){
            this.name = a;
            this.cost = b;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }
    }
}
