package com.example.matthewspc.financeapp;

/**
 * Created by Christine on 12/3/2017.
 */

public class GroupObject {
    private String groupId;
    private String groupName;
    private String groupGoal;
    private String groupAmount;
    public GroupObject() {

    }

    public GroupObject(String groupId, String groupName, String groupGoal) {
        this.groupId=groupId;
        this.groupName = groupName;
        this.groupGoal = groupGoal;
        this.groupAmount = "0";
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupGoal(){
        return groupGoal;
    }
    public String getGroupAmount(){
        return groupAmount;
    }
    public void setGroupAmount(String groupAmount) {
        this.groupAmount = groupAmount;
    }
}
