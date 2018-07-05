package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GroupFunding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_funding);
    }

    public void submit(View view) //defines listener for the UpdateProfile Activity
    {
        final EditText donationInput = (EditText) findViewById(R.id.groupFundingInput);
        Intent intent = new Intent();
        intent.putExtra("amount", donationInput.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancel(View view)//defines listener for the Groups Activity
    {
        finish();
        //the listener is defined in the XML file
    }
}
