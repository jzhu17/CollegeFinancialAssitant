package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IndividualGroupMenu extends AppCompatActivity {


    private TextView textGroupProgress;
    private DatabaseReference databaseGroup;
    private String groupId;
    private String groupName;
    private String groupGoal;
    private String groupAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_group_menu);

        textGroupProgress = (TextView) findViewById(R.id.textGroupProgress);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("GroupId");
        groupName = intent.getStringExtra("GroupName");
        groupGoal = intent.getStringExtra("GroupGoal");
        groupAmount = intent.getStringExtra("GroupAmount");

        textGroupProgress.setText("$"+groupAmount+"/$" + groupGoal);
        databaseGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupId);
    }

    public void groupFunding(View view) //defines listener for the Add to Funding Activity
    {
        startActivityForResult(new Intent(IndividualGroupMenu.this, GroupFunding.class), 1);
    }

    public void back(View view)//defines listener for the back button
    {
        Intent intent = new Intent();
        intent.putExtra("amount", groupAmount);
        intent.putExtra("goal", groupGoal);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void finishProject(View view){//defines listener for finish project button
        //the listener is defined in the XML file. Currently does nothing.
        databaseGroup.removeValue();
        finish();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //when we come back from the Group Funding activity
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String donation = data.getStringExtra("amount");//get amount from funding page
                GroupObject group = new GroupObject(groupId, groupName, groupGoal);
                double currentValue = Double.parseDouble(groupAmount);
                double donationValue = Double.parseDouble(donation);
                double total = currentValue + donationValue;
                groupAmount= total+"";
                group.setGroupAmount(groupAmount);
                databaseGroup.setValue(group);
                textGroupProgress.setText("$"+groupAmount+"/$" + groupGoal);
            }
        }
    }
}
