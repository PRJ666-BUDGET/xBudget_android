package com.prj666_183a06.xbudget.ExpenseRoom;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListInterface {
    private List<ExpenseObj> list;
    private ExpenseObj temp;
    ExpenseViewModel vm;

    public ExpenseListInterface(ExpenseViewModel e){
        list = new ArrayList<>();
        vm = e;
        list = vm.getAll();
    }

    public String print(){
        String ret = "";
        for(ExpenseObj r: list){
            ret = ret + r.getItem() + " " + r.getStore() + " " + r.getDate() + " " + r.getCost() + "\n";
        }
        return ret;
    }

    public double costTotal(){
        double ret = 0;
        for(ExpenseObj c: list){
            ret += c.getCost();
        }
        return ret;
    }
}
