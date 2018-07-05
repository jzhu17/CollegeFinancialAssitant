package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.toIntExact;

public class UpdateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        add();
        cancel();
    }
    public void add(){//this is the submission button method
        Button btn = (Button) findViewById(R.id.updateSubmitButton);
        final EditText spend_goal = (EditText) findViewById(R.id.goalSpendInput);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Date date= new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                if(!spend_goal.getText().toString().equals("")
                        &&(datePicker.getYear()>year
                        ||((datePicker.getYear()>=year)&&(datePicker.getMonth()>month))
                        ||((datePicker.getDayOfMonth()>day)&&(datePicker.getMonth()>=month)&&(datePicker.getYear()>=year))))
                {
                    intent.putExtra("spendGoal", spend_goal.getText().toString());
                    intent.putExtra("spendDate", (datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth()+"/"+datePicker.getYear());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    //throw eror
                }
            }
        });
    }
    public void cancel(){//if we don't want to put anything in
        //we return nothing
        Button btn = (Button) findViewById(R.id.updateCancelButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
