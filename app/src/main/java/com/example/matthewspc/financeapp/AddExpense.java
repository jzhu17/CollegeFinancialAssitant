package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        add();
        cancel();
        Spinner s1 = (Spinner)findViewById(R.id.tagInput);
        String[] xData = new String[]{"Groceries", "Dining Out","Rent","Utilities","Travel","Clothes","Other"};
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, xData);
        s1.setAdapter(arrayAdapter);
    }
    public void add(){//this is the submission button method
        Button btn = (Button) findViewById(R.id.addExpenseSubmit);
        final EditText nameInput = (EditText) findViewById(R.id.NameInput);
        final EditText costInput = (EditText) findViewById(R.id.CostInput);
        final Spinner tagInput = (Spinner) findViewById(R.id.tagInput);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here we send back anything needed to make the entry object
                Intent intent = new Intent();
                intent.putExtra("purchaseName", nameInput.getText().toString());
                intent.putExtra("purchaseCost", costInput.getText().toString());
                intent.putExtra("purchaseTag", tagInput.getSelectedItem().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    public void cancel(){//if we don't want to put anything in
        //we return nothing
        Button btn = (Button) findViewById(R.id.addExpenseCancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
