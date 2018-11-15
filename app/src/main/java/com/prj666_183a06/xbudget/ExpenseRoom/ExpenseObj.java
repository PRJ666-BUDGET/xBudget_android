package com.prj666_183a06.xbudget.ExpenseRoom;

public class ExpenseObj {
    private String item;
    private String store;
    private String date;
    private double cost;

    public ExpenseObj(String item, String store, String date, double cost){
        this.item = item;
        this.store = store;
        this.date = date;
        this.cost = cost;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String print(){
        return item + " " + store + " " + date  + " " + cost + "\n" + "";
    }
}
