package com.example.matthewspc.financeapp;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Beast_07 on 12/2/2017.
 */

public class ExpenseLogEntryData {
    String name;
    String cost;
    String date;
    String tag;

    String getName(){
            return name;
        }
    String getCost(){
            return cost;
        }
    String getDate(){ return date; }
    String getTag() { return tag;}
    void setName(String newName){
            name = newName;
        }
    void setCost(String newCost){
            cost = newCost;
        }
    void setTimedate(){
        Date c = Calendar.getInstance().getTime();
        date = String.valueOf(c);
    }
    void setTag(String newTag) {tag = newTag;}

    public ExpenseLogEntryData(String h, String n, String tt){
        setName(h);
        setCost(n);
        setTimedate();
        setTag(tt);
    }

    public ExpenseLogEntryData(String h, String n, String TD, String tt){
        setName(h);
        setCost(n);
        date = TD;
        setTag(tt);
    }


}