package com.example.matthewspc.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupProfilePage extends AppCompatActivity implements View.OnClickListener{
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private String userId;

    //view objects
    private TextView textViewUserEmail;
    private TextView textViewCost;
    private Button buttonLogout;
    //private Button buttonJoinGroup;
    private Button buttonCreateGroup;
    private ListView listViewGroups;
    public GroupAdapter adapter;

    private List<GroupObject> groupList;
    private String amount;

    private DatabaseReference databaseGroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile_page);
        Log.d("test", "in group page");
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginPage.class));
        }
        userId = firebaseAuth.getUid();
        databaseGroups= FirebaseDatabase.getInstance().getReference("groups");
        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        //buttonJoinGroup =(Button) findViewById(R.id.buttonJoinGroup);
        buttonCreateGroup = (Button) findViewById(R.id.buttonCreateGroup);
        listViewGroups = (ListView) findViewById(R.id.listViewGroups);
        //adapter = new GroupAdapter(this);
        //listViewGroups.setAdapter(adapter);
        groupList = new ArrayList<>();

        //displaying logged in user name
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList.clear();

                for(DataSnapshot groupSnapshot: dataSnapshot.getChildren()){
                    GroupObject groups = groupSnapshot.getValue(GroupObject.class);
                    groupList.add(groups);
                }

                GroupAdapter adapter = new GroupAdapter(GroupProfilePage.this, groupList){
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        LayoutInflater inflater = GroupProfilePage.this.getLayoutInflater();
                        View listViewItem = inflater.inflate(R.layout.grouplist_layout, null ,true);
                        TextView textViewGroupName = (TextView) listViewItem.findViewById(R.id.textViewGroupName);
                        TextView textViewCost = (TextView) listViewItem.findViewById(R.id.textViewCost);

                        final GroupObject groupObject = groupList.get(position);
                        Button move = (Button) listViewItem.findViewById(R.id.viewButton);
                        move.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myintent=new Intent(GroupProfilePage.this, IndividualGroupMenu.class).putExtra("GroupId", groupObject.getGroupId());
                                myintent.putExtra("GroupName", groupObject.getGroupName());
                                myintent.putExtra("GroupGoal", groupObject.getGroupGoal());
                                myintent.putExtra("GroupAmount", groupObject.getGroupAmount());
                                //startActivity(myintent);
                                startActivity(myintent);
                            }
                        });
                        textViewGroupName.setText("Group Name: "+groupObject.getGroupName());
                        textViewCost.setText("Goal: $"+groupObject.getGroupGoal());
                        return listViewItem;
                    }
                };
                listViewGroups.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void joinGroup(View view) //defines listener for the Join Group Activity
    {
        startActivityForResult(new Intent(GroupProfilePage.this, JoinGroup.class), 1);
    }

    public void createGroup(View view)//defines listener for the Create Group Activity
    {
        //the listener  is defined in the XML file
        startActivityForResult(new Intent(GroupProfilePage.this, CreateGroup.class), 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //when we come back from the Join Group activity
        /*if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String goal =data.getStringExtra("goal");
                String amount =data.getStringExtra("amount");

                this.amount = amount;

                //databaseGroups.child(userId)..setValue()
                //databaseGroups.child(id).setValue();
            }
        }
        else */
        if (requestCode == 2){//or from the Create Group Activity
            if(resultCode == RESULT_OK) {

                String groupName = data.getStringExtra("name");
                String groupGoal = data.getStringExtra("goal");

                String id = databaseGroups.push().getKey();
                GroupObject group = new GroupObject(id, groupName, groupGoal);

                databaseGroups.child(id).setValue(group);
                databaseGroups.child(id).child("members").setValue(userId);
                Toast.makeText(this, "Group created", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginPage.class));
        }
    }
}
