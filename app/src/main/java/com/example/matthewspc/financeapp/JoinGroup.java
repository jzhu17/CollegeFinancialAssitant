package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    public void submit(View view)//defines listener for Submit button
    {
        //the listener  is defined in the XML file
        final EditText key = (EditText) findViewById(R.id.joinGroupInput);
        Intent intent = new Intent();
        intent.putExtra("key", key.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
    public void cancel(View view)//defines listener for Cancel Button
    {
        //the listener  is defined in the XML file
        finish();
    }
}
