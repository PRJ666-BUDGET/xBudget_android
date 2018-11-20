package com.prj666_183a06.xbudget.ExpenseRoom;

import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseListInterface {
    private List<ExpenseObj> listE;
    private List<String> titles;

    private ExpenseObj temp;
    ExpenseViewModel vm;
    PlanViewModel pvm;

    public ExpenseListInterface(ExpenseViewModel e){
        listE = new ArrayList<>();
        vm = e;
        listE = vm.getAll();
        titles = new ArrayList<>();
    }

    public ExpenseListInterface(ExpenseViewModel e, PlanViewModel p){
        listE = new ArrayList<>();

        vm = e;
        pvm = p;
        listE = vm.getAll();
        titles = pvm.getTitleList();
    }

    public String print(){
        String ret = "";
        for(ExpenseObj r: listE){
            ret = ret + r.getItem() + " " + r.getStore() + " " + r.getDate() + " " + r.getCost() + "\n";
        }
        return ret;
    }

    public double costTotal(){
        double ret = 0;
        for(ExpenseObj c: listE){
            ret += c.getCost();
        }
        return ret;
    }

    public List<Float> costTotalByCategory(){
        List<Float> templist= new ArrayList<>();
        float temp = 0;
        if(!titles.isEmpty()) {
            for(String r: titles){
                for(ExpenseObj c: listE){
                    if(r.equals(c.getCategory())){
                        temp += (float)c.getCost();
                    }
                }
                templist.add(temp);
                temp = 0;
            }
        }
        return templist;
    }

    public List<Float> getCostByDaily(){
        List<Float> tempList= new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        float temp;

        for(int i = 0; i < 30; i++){
            temp = 0;
            for(ExpenseObj c: listE){
                if(c.getDate().equals(month + "/" + day + "/" + year)){
                    temp += (float) c.getCost();
                }
            }

            tempList.add(temp);
            if(day != 1){
                day--;
            }else{
                if(month!=1){
                    month--;
                    day = 31;
                }else{
                    year--;
                    month = 12;
                    day = 31;
                }
            }
            System.out.println(month + "/" + day + "/" + year);
        }
        return tempList;
    }
}
