package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroup extends AppCompatActivity {

    private EditText createGroupNameInput;
    private EditText createGroupGoalInput;
    private Button createGroupSubmit;
    private Button createGroupCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        createGroupCancel = (Button) findViewById(R.id.createGroupCancel);
        createGroupSubmit=(Button) findViewById(R.id.createGroupSubmit);
        createGroupNameInput=(EditText) findViewById(R.id.createGroupNameInput);
        createGroupGoalInput=(EditText) findViewById(R.id.createGroupGoalInput);

    }

    public void submit(View view)//defines listener for Submit button
    {
        //the listener  is defined in the XML file
        final EditText name = (EditText) findViewById(R.id.createGroupNameInput);
        final EditText goal = (EditText) findViewById(R.id.createGroupGoalInput);

        String nameString = name.getText().toString().trim();
        String goalString = name.getText().toString().trim();

        if(TextUtils.isEmpty(nameString)){
            Toast.makeText(this, "Please enter a group name.", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(goalString)){
            Toast.makeText(this, "Please enter a goal amount.", Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent();
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("goal", goal.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    public void cancel(View view)//defines listener for Cancel Button
    {
        //the listener  is defined in the XML file
        finish();
    }

}
